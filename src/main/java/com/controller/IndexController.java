package com.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.constant.Constants;
import com.model.BlockInfo;
import com.model.Transaction;
import com.service.JfService;
import com.util.HttpClientUtil;
import com.util.ProConfig;
import com.util.Utils;

@Controller
public class IndexController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	JfService jfService;
	
	@RequestMapping("/")
    public String index(HttpServletRequest request,Model model) {
		int begin = 0;
		int size = 6;
		List<BlockInfo> blockInfoList = jfService.getBlockInfo(begin,size);
		model.addAttribute("blockInfoList", blockInfoList);
		
		List<Transaction> transList = jfService.getTransactionInfo(begin,size,null);
		model.addAttribute("transactionInfoList", transList);
        return "index";
    }
	
	@RequestMapping("/top")
    public @ResponseBody Map<String,Object> top(HttpServletRequest request) {
		String blockNumber = jfService.getBlockNumber();
		String peerCount = jfService.getPeerCount();
		
		String method ="net_listening";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
				"\"params\""+":[],"+"\"id\""+":"+87+"}";
		JSONObject resultObj = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
		String result = resultObj.getString("result");
		if(result=="true"){
			result = "Running";
		}else{
			result = "Stopped";
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("blockNumber", blockNumber);
		map.put("peerCount", peerCount);
		map.put("peerStatu", result);
        return map;
    }
	
	@RequestMapping("/blockInfo")
	public String blockInfo(HttpServletRequest request,Model model){
		int blockCounts = jfService.getBlockCounts();
		int pageNo = Integer.parseInt(request.getParameter("pageNo")==null?String.valueOf(Constants.PAGENO):request.getParameter("pageNo"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize")==null?String.valueOf(Constants.PAGESIZE):request.getParameter("pageSize")); 
		
		List<BlockInfo> resultList = jfService.getBlockInfo((pageNo-1)*pageSize,pageSize);
		model.addAttribute("blockInfoList", resultList);
		model.addAttribute("blockNumber", blockCounts);
		model.addAttribute("pageNo", pageNo);
		return "block";
	}
	
	@RequestMapping("/transactionInfo")
	public String transactionInfo(HttpServletRequest request,Model model){
		int transCounts = jfService.getTransactionCounts();
		int pageNo = Integer.parseInt(request.getParameter("pageNo")==null?String.valueOf(Constants.PAGENO):request.getParameter("pageNo"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize")==null?String.valueOf(Constants.PAGESIZE):request.getParameter("pageSize")); 
		List<String> searchList = null;
		if(request.getParameter("hash") != null){
			String search = request.getParameter("hash");
			String[] str = search.split(",");
			searchList = Arrays.asList(str);
		}
		
		List<Transaction> resultList = jfService.getTransactionInfo((pageNo-1)*pageSize,pageSize,searchList);
		model.addAttribute("transactionInfoList", resultList);
		model.addAttribute("transactionCounts", transCounts);
		model.addAttribute("pageNo", pageNo);
		return "trade";
	}
	
	@RequestMapping("/blockDetail")
	public String blockDetail(HttpServletRequest request,Model model){
		String hash = request.getParameter("hash");
		String method ="eth_getBlockByHash";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
				"\"params\""+":[\""+hash+"\",true],"+"\"id\""+":"+87+"}";
		JSONObject resultObj = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
		JSONObject resultJson = JSONObject.parseObject(resultObj.getString("result"));
		
		BlockInfo blockInfo = new BlockInfo();
		String difficulty = String.valueOf(Long.parseLong(resultJson.getString("difficulty").replaceAll("^0[x|X]", ""), 16));
    	String extraData = resultJson.getString("extraData");
    	String gasLimit = String.valueOf(Long.parseLong(resultJson.getString("gasLimit").replaceAll("^0[x|X]", ""), 16));
    	String gasUsed = String.valueOf(Long.parseLong(resultJson.getString("gasUsed").replaceAll("^0[x|X]", ""), 16));
    	String b_hash = resultJson.getString("hash");
    	String logsBloom = resultJson.getString("logsBloom");
    	String miner = resultJson.getString("miner");
    	String mixHash = resultJson.getString("mixHash");
    	String nonce = resultJson.getString("nonce");
    	String number = String.valueOf(Long.parseLong(resultJson.getString("number").replaceAll("^0[x|X]", ""), 16));
    	String parentHash = resultJson.getString("parentHash");
    	String receiptsRoot = resultJson.getString("receiptsRoot");
    	String sha3Uncles = resultJson.getString("sha3Uncles");
    	String size = String.valueOf(Long.parseLong(resultJson.getString("size").replaceAll("^0[x|X]", ""), 16));
    	String stateRoot = resultJson.getString("stateRoot");
    	String timestamp = String.valueOf(Long.parseLong(resultJson.getString("timestamp").replaceAll("^0[x|X]", ""), 16));
    	String totalDifficulty = String.valueOf(Long.parseLong(resultJson.getString("totalDifficulty").replaceAll("^0[x|X]", ""), 16));
    	String transactionsRoot = resultJson.getString("transactionsRoot");
    	
    	blockInfo.setDifficulty(difficulty);
    	blockInfo.setExtraData(extraData);
    	blockInfo.setGasLimit(gasLimit);
    	blockInfo.setGasUsed(gasUsed);
    	blockInfo.setHash(b_hash);
    	blockInfo.setLogsBloom(logsBloom);
    	blockInfo.setMiner(miner);
    	blockInfo.setMixHash(mixHash);
    	blockInfo.setNonce(nonce);
    	blockInfo.setNumber(number);
    	blockInfo.setParentHash(parentHash);
    	blockInfo.setReceiptsRoot(receiptsRoot);
    	blockInfo.setSha3Uncles(sha3Uncles);
    	blockInfo.setSize(size);
    	blockInfo.setStateRoot(stateRoot);
    	blockInfo.setTimestamp(Utils.stampToDate(timestamp));
    	blockInfo.setTotalDifficulty(totalDifficulty);
    	blockInfo.setTransactionsRoot(transactionsRoot);
    	
    	JSONArray transArray = JSONArray.parseArray(resultJson.getString("transactions"));
    	List<Transaction> transList = new ArrayList<Transaction>();
    	for(int t=0;t<transArray.size();t++){
    		JSONObject transObj = JSONObject.parseObject(transArray.getString(t));
    		String blockNumber = String.valueOf(Long.parseLong(transObj.getString("blockNumber").replaceAll("^0[x|X]", ""), 16));
    		String gas = String.valueOf(Long.parseLong(transObj.getString("gas").replaceAll("^0[x|X]", ""), 16));
    		BigInteger gg = new BigInteger(transObj.getString("gasPrice").replaceAll("^0[x|X]", ""), 16).divide(Constants.BASE_COIN_GAS);
    		String gasPrice = String.valueOf(gg);
    		String t_nonce = String.valueOf(Long.parseLong(transObj.getString("nonce").replaceAll("^0[x|X]", ""), 16));
    		String transactionIndex = String.valueOf(Long.parseLong(transObj.getString("transactionIndex").replaceAll("^0[x|X]", ""), 16));
    		BigDecimal bb = new BigDecimal(new BigInteger(transObj.getString("value").replaceAll("^0[x|X]", ""), 16).doubleValue());
    		String value = String.valueOf(bb.divide(Constants.BASE_COIN, BigDecimal.ROUND_HALF_DOWN));
    		
    		Transaction transaction = new Transaction();
    		transaction.setBlockHash(transObj.getString("blockHash"));
    		transaction.setBlockNumber(blockNumber);
    		transaction.setFrom(transObj.getString("from"));
    		transaction.setGas(gas);
    		transaction.setGasPrice(gasPrice);
    		transaction.setHash(transObj.getString("hash"));
    		transaction.setInput(transObj.getString("input"));
    		transaction.setNonce(t_nonce);
    		transaction.setR(transObj.getString("r"));
    		transaction.setS(transObj.getString("s"));
    		transaction.setTo(transObj.getString("to"));
    		transaction.setTransactionIndex(transactionIndex);
    		transaction.setV(transObj.getString("v"));
    		transaction.setValue(value);
    		transList.add(transaction);
    	}
    	blockInfo.setTransactions(transList);
    	blockInfo.setTransSize(String.valueOf(transList.size()));
    	
    	JSONArray unclesArray = JSONArray.parseArray(resultJson.getString("uncles"));
    	List<String> uncleList = new ArrayList<String>();
    	for(int u=0;u<unclesArray.size();u++){
    		uncleList.add(unclesArray.getString(u));
    	}
    	blockInfo.setUncles(uncleList);
		
    	model.addAttribute("blockInfo", blockInfo);
		return "block_detail";
	}
	
	@RequestMapping("/transactionDetail")
	public String transactionDetail(HttpServletRequest request,Model model){
		String hash = request.getParameter("hash");
		String method ="eth_getTransactionByHash";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
				"\"params\""+":[\""+hash+"\"],"+"\"id\""+":"+87+"}";
		JSONObject resultJson = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
		JSONObject transObj = JSONObject.parseObject(resultJson.getString("result"));
		String blockNumber = String.valueOf(Long.parseLong(transObj.getString("blockNumber").replaceAll("^0[x|X]", ""), 16));
		String gas = String.valueOf(Long.parseLong(transObj.getString("gas").replaceAll("^0[x|X]", ""), 16));
		BigInteger gg = new BigInteger(transObj.getString("gasPrice").replaceAll("^0[x|X]", ""), 16).divide(Constants.BASE_COIN_GAS);
		String gasPrice = String.valueOf(gg);
		String t_nonce = String.valueOf(Long.parseLong(transObj.getString("nonce").replaceAll("^0[x|X]", ""), 16));
		String transactionIndex = String.valueOf(Long.parseLong(transObj.getString("transactionIndex").replaceAll("^0[x|X]", ""), 16));
		BigDecimal bb = new BigDecimal(new BigInteger(transObj.getString("value").replaceAll("^0[x|X]", ""), 16).doubleValue());
		String value = String.valueOf(bb.divide(Constants.BASE_COIN, BigDecimal.ROUND_HALF_DOWN));
		
		Transaction transaction = new Transaction();
		transaction.setBlockHash(transObj.getString("blockHash"));
		transaction.setBlockNumber(blockNumber);
		transaction.setFrom(transObj.getString("from"));
		transaction.setGas(gas);
		transaction.setGasPrice(gasPrice);
		transaction.setHash(transObj.getString("hash"));
		transaction.setInput(Utils.hexStr2Str(transObj.getString("input").replaceAll("^0[x|X]", "")));
		transaction.setNonce(t_nonce);
		transaction.setR(transObj.getString("r"));
		transaction.setS(transObj.getString("s"));
		transaction.setTo(transObj.getString("to"));
		transaction.setTransactionIndex(transactionIndex);
		transaction.setV(transObj.getString("v"));
		transaction.setValue(value);
		
    	model.addAttribute("transactionInfo", transaction);
		return "trade_detail";
	}

	@RequestMapping("/getBlockByHash")
	public @ResponseBody String getBlockByHash(HttpServletRequest request,Model model){
		String hash = request.getParameter("hash");
		String method ="eth_getBlockByHash";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
				"\"params\""+":[\""+hash+"\",true],"+"\"id\""+":"+87+"}";
		return HttpClientUtil.httpPostEth(BASE_URI,str);
	}
	
	@RequestMapping("/search")
	public @ResponseBody String search(HttpServletRequest request,Model model){
		String result = "";
		String param = request.getParameter("param");
		if(param.indexOf("0x")>-1){
			
			List<Transaction> list = jfService.getTransByAccount(param);
			if(list.size()>0){//根据钱包地址搜索
				String transStr = "";
				for(Transaction transaction : list){
					String hash = transaction.getHash();
					transStr += hash + ",";
				}
				result = "/transactionInfo?"+transStr.substring(0, transStr.length()-1);
			}else{//根据交易hash搜索
				String method ="eth_getTransactionByHash";
				String BASE_URI = ProConfig.getJfsc_eth_url();
				String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
						"\"params\""+":[\""+param+"\"],"+"\"id\""+":"+87+"}";
				JSONObject resultJson = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
				if(resultJson.getString("result")==null){
					result = "error";
				}else{
					result = "/transactionDetail?"+param;
				}
			}
		}else{
			String method ="eth_getBlockByNumber";
			String BASE_URI = ProConfig.getJfsc_eth_url();
			String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
					"\"params\""+":[\"0x"+Integer.toHexString(Integer.parseInt(param))+"\",true],"+"\"id\""+":"+87+"}";
			JSONObject resultObj = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
			if(resultObj.getString("result")==null){
				result = "error";
			}else{
				JSONObject resultJson = JSONObject.parseObject(resultObj.getString("result"));
				result = "/blockDetail?"+resultJson.getString("hash");
			}
		}
		return result;
	}
    
}
