package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.constant.Constants;
import com.model.AllList;
import com.model.Test;
import com.model.UserListForm;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.ConnectionInfo;
import com.trilead.ssh2.SCPClient;
import com.util.HttpClientUtil;
import com.util.ProConfig;
import com.util.Utils;

@Controller
public class JfController {
	static Logger log = LoggerFactory.getLogger(JfController.class);
	public static final int UNLOCAK_TIME = 300;//解锁间隔时间（s）
	
	@RequestMapping("/test")
    public String test(UserListForm userForm) {
		
		return "test";
    }
	
	@RequestMapping("/newAccount")
    public @ResponseBody String getBlock(HttpServletRequest request) {
		String passwd = request.getParameter("passwd");
		if(passwd==null || passwd==""){
			return "请输入密码";
		}
		String method ="personal_newAccount";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
				"\"params\""+":[\""+passwd+"\"],"+"\"id\""+":"+87+"}";
		return HttpClientUtil.httpPostEth(BASE_URI,str);
    }
	
	@RequestMapping("/putOrderToChain")
    public @ResponseBody String putOrderToChain(HttpServletRequest request) {
		String BASE_URI = ProConfig.getJfsc_eth_url();
		Map<String, String[]> reqMap = request.getParameterMap();
		String fromAccount = request.getParameter("fromAccount");
		String toAccount = request.getParameter("toAccount");
		String fromPasswd = request.getParameter("fromPasswd");
//		String toPasswd = request.getParameter("toPasswd");
		String transferValue = request.getParameter("transferValue");
		String dataJson = request.getParameter("dataJson");
		log.info("reqMap: "+ reqMap);
		log.info("fromAccount: "+fromAccount+",toAccount: "+toAccount+",fromPasswd: "+fromPasswd+",transferValue: "+transferValue+",dataJson: "+dataJson);
		
		//调用以太坊接口转账交易
		JSONObject objJson = new JSONObject();
		objJson.put("jsonrpc", "2.0");
		objJson.put("method", "eth_sendTransaction");
		JSONArray paramArray = new JSONArray();
		JSONObject paramJson = new JSONObject();
		paramJson.put("from", fromAccount);
		paramJson.put("to", toAccount);
		paramJson.put("gas", "0x76c0");
		paramJson.put("gasPrice", "0x0");
		try {
			paramJson.put("value", "0x" + Integer.parseInt(transferValue));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			log.error("putOrderToChain转换value失败，原因：" + e.getMessage());
			return e.getMessage();
		}
		if(dataJson!=null){
			paramJson.put("data", "0x" + Utils.str2HexStr(dataJson));
		}
		paramArray.add(paramJson);
		objJson.put("params", paramArray);
		objJson.put("id", 82);
		if(!unlockAccount(fromAccount,fromPasswd)){
			return "账户：" + fromAccount + " 解锁失败！";
		}
//		if(!unlockAccount(toAccount,toPasswd)){
//			return "账户：" + fromAccount + " 解锁失败！";
//		}
		
		return HttpClientUtil.httpPostEth(BASE_URI,objJson.toString());
    }
	
	@RequestMapping("/getOrderByHash")
    public @ResponseBody String getOrderByHash(HttpServletRequest request) {
		String transHash = request.getParameter("transHash");
		String method ="eth_getTransactionByHash";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
				"\"params\""+":[\""+transHash+"\"],"+"\"id\""+":"+87+"}";
		
		JSONObject resultJson = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
		String result = resultJson.getString("result");
		JSONObject etheJson = JSONObject.parseObject(result);
		String data = etheJson.getString("input");
		log.info("getOrderByHash resultJson: " + resultJson);
		String from = etheJson.getString("from");
		String to = etheJson.getString("to");
		BigDecimal bb = new BigDecimal(new BigInteger(etheJson.getString("value").replaceAll("^0[x|X]", ""), 16).doubleValue());
		String value = String.valueOf(bb.divide(Constants.BASE_COIN, BigDecimal.ROUND_HALF_DOWN));
		String hash = etheJson.getString("hash");
		
		JSONObject returnJson = new JSONObject(); 
		returnJson.put("transHash", hash);
		returnJson.put("fromAccount", from);
		returnJson.put("toAccount", to);
		returnJson.put("transferValue", value);
		returnJson.put("dataJson", Utils.hexStr2Str(data.replaceAll("^0[x|X]", "")));
		
		return returnJson.toString();
    }
	
	@RequestMapping("/getBalance")
    public @ResponseBody String getBalance(HttpServletRequest request) {
		String account = request.getParameter("account");
		String method ="eth_getBalance";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
				"\"params\""+":[\""+account+"\",\"latest\"],"+"\"id\""+":"+87+"}";
		JSONObject resultJson = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
		String result = resultJson.getString("result");
		JSONObject jsonObject = new JSONObject();
		if(result!=null){
			jsonObject.put("result", "success");
			BigDecimal bb = new BigDecimal(new BigInteger(result.replaceAll("^0[x|X]", ""), 16).doubleValue());
			String value = String.valueOf(bb.divide(Constants.BASE_COIN, BigDecimal.ROUND_HALF_DOWN));
			jsonObject.put("balance", value);
		}else{
			jsonObject.put("result", "error");
			jsonObject.put("balance", null);
		}
		return jsonObject.toString();
    }
	
	public boolean unlockAccount(String account,String passwd){
		String BASE_URI = ProConfig.getJfsc_eth_url();
		//解锁账户
		String method1 ="personal_unlockAccount";
		String param1 = account;
		String params = passwd;
		int param2 = UNLOCAK_TIME;
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method1+"\","+
				"\"params\""+":[\""+param1+"\",\""+params+"\","+param2+"],"+"\"id\""+":"+87+"}";
		String result = HttpClientUtil.httpPostEth(BASE_URI, str);
		JSONObject resultJson = (JSONObject) JSONObject.parse(result);
		if(resultJson.getString("result")==null){
			log.error("交易账户解锁失败！账户：" + param1 +" <<<" + result);
			return false;
		}
		return true;
	}
	
	@RequestMapping("/alterMinerDiff")
    public @ResponseBody String alterMinerDiff(HttpServletRequest request) {
		String result = "success";
		String difficulty = request.getParameter("difficulty");
		if(difficulty==null || difficulty==""){
			result = "error";
		}
		String diffPath = ProConfig.getJfsc_diff_path();
		File file = null;
		Properties props=System.getProperties();
		String os = props.getProperty("os.name");
		if(os.indexOf("indows") > -1){
			file = new File(ProConfig.getJfsc_genesis_file_win_path());
		}else{
			file = new File(ProConfig.getJfsc_genesis_file_linux_path());
		}
		InputStream inputStream=this.getClass().getResourceAsStream("/file/genesis.json");
		boolean i_flag = inputstreamToFile(inputStream,file);
//		file = ResourceUtils.getFile("classpath:file/genesis.json");
		boolean r_flag = replaceContent(file,difficulty);
		boolean s_flag = ssh2(file.getPath(),diffPath);
		if(!r_flag || !s_flag || !i_flag){
			result = "error";
		}
		return result;
    }
	
	public boolean replaceContent(File file,String repContent) {
		boolean flag = true;
		Long fileLength = file.length();
		byte[] fileContext = new byte[fileLength.intValue()];
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			in.read(fileContext);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("读取文件出错，原因：" + e.getMessage());
			flag = false;
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.error("关闭读取文件流出错，原因：" + e.getMessage());
				flag = false;
			}
		}
		
		String str = new String(fileContext);
		JSONObject jstr = JSONObject.parseObject(str);
		jstr.remove("difficulty");
		jstr.put("difficulty", "0x" + repContent);
		str = jstr.toString();
		PrintWriter out = null;
		try {
			out = new PrintWriter(file.getPath());
			out.write(str.toCharArray());
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("PrintWriter写入文件出错，原因：" + e.getMessage());
			flag = false;
		}finally {
			out.close();
		}
		
		return flag;
	}
	
	@SuppressWarnings("unused")
	public boolean ssh2(String localFilePath,String remoteFilePath){
		boolean flag = true;
		Connection con = new Connection(ProConfig.getJfsc_server_ip(), Integer.parseInt(ProConfig.getJfsc_server_port())); 
		try {
			ConnectionInfo connect = con.connect(); 
			boolean isAuthed = con.authenticateWithPassword(ProConfig.getJfsc_server_username(),ProConfig.getJfsc_server_passwd());
			SCPClient scpClient = con.createSCPClient(); 
			//将本地文件上传到服务器 
			scpClient.put(localFilePath, remoteFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("上传文件到服务器出错，原因：" + e.getMessage());
			flag = false;
		}finally {
			con.close();
		}
		return flag;
	}
	
	public boolean inputstreamToFile(InputStream ins,File file) {
		boolean flag = true;
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("OutputStream读文件出错，原因：" + e.getMessage());
			flag = false;
		}finally{
			try {
				ins.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.error("关闭流OutputStream读文件出错，原因：" + e.getMessage());
				flag = false;
			}
		}
		return flag;
	}

}
