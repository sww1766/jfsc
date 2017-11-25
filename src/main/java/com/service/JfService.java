package com.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.constant.Constants;
import com.mapper.JfscMapper;
import com.model.BlockInfo;
import com.model.Query;
import com.model.Transaction;
import com.util.HttpClientUtil;
import com.util.ProConfig;
import com.util.Utils;

@Service
public class JfService {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JfscMapper jfscMapper;
	
	/**
	 * 查交易表总数
	 * @return
	 */
	public int getTransactionCounts(){
		return jfscMapper.getTransactionCounts();
	}
	
	/**
	 * 查block表总数
	 * @return
	 */
	public int getBlockCounts(){
		return jfscMapper.getBlockCounts();
	}

	/**
	 * 查询区块数量
	 * @return
	 */
	public String getBlockNumber(){
		String method ="eth_blockNumber";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
				"\"params\""+":[],"+"\"id\""+":"+87+"}";
	    JSONObject resultObj = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
	    String result = String.valueOf(Long.parseLong(resultObj.getString("result").replaceAll("^0[x|X]", ""), 16));
	    return result;
	}
	
	/**
	 * 查询节点数量
	 * @return
	 */
	public String getPeerCount(){
		String method ="net_peerCount";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
				"\"params\""+":[],"+"\"id\""+":"+87+"}";
	    JSONObject resultObj = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
	    String result = String.valueOf(Long.parseLong(resultObj.getString("result").replaceAll("^0[x|X]", ""), 16));
	    return result;
	}
	
	/**
	 * 查询区块信息
	 * @param start 开始位置，从0计算
	 * @param count 查询数量
	 * @return
	 */
	public List<BlockInfo> getBlockInfo(int start,int count){
		String method ="eth_getBlockByHash";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		List<BlockInfo> list = new ArrayList<BlockInfo>();
		
		Query query = new Query();
		query.setStart(start);
		query.setCount(count);
		List<BlockInfo> blockList = jfscMapper.getBlockByIndex(query);
		for(BlockInfo blockInfo : blockList){
			String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
					"\"params\""+":[\""+blockInfo.getHash()+"\",true],"+"\"id\""+":"+87+"}";
		    JSONObject resultObj = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
		    if(resultObj!=null && resultObj.getString("result") != null){
		    	JSONObject resultJson = JSONObject.parseObject(resultObj.getString("result"));
		    	String difficulty = String.valueOf(Long.parseLong(resultJson.getString("difficulty").replaceAll("^0[x|X]", ""), 16));
		    	String extraData = resultJson.getString("extraData");
		    	String gasLimit = String.valueOf(Long.parseLong(resultJson.getString("gasLimit").replaceAll("^0[x|X]", ""), 16));
		    	String gasUsed = String.valueOf(Long.parseLong(resultJson.getString("gasUsed").replaceAll("^0[x|X]", ""), 16));
		    	String hash = resultJson.getString("hash");
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
		    	blockInfo.setHash(hash);
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
		    		String gasPrice = String.valueOf(Long.parseLong(transObj.getString("gasPrice").replaceAll("^0[x|X]", ""), 16));
		    		String t_nonce = String.valueOf(Long.parseLong(transObj.getString("nonce").replaceAll("^0[x|X]", ""), 16));
		    		String transactionIndex = String.valueOf(Long.parseLong(transObj.getString("transactionIndex").replaceAll("^0[x|X]", ""), 16));
		    		BigDecimal bb = new BigDecimal(new BigInteger(transObj.getString("value").replaceAll("^0[x|X]", ""), 16).doubleValue());
		    		String value = String.valueOf(bb.divide(Constants.BASE_COIN, BigDecimal.ROUND_HALF_DOWN));
		    		
		    		Transaction transaction = new Transaction();
		    		int id = jfscMapper.getMaxIdFromTransaction();
		    		transaction.setId(id);
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
		    		transaction.setTimestamp(timestamp);
		    		
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
		    	
		    	list.add(blockInfo);
		    }
		}
		
		return list;
	}
	
	/**
	 * 采集区块和交易信息
	 * @param start 开始位置,从最新采集算起
	 * @param count 查询数量
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public void collectInfo(){
		int maxBlockNumber = jfscMapper.getMaxIdFromBlock()-1;
		int blockConts = Integer.parseInt(getBlockNumber());
		String method ="eth_getBlockByNumber";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		int begin = 0;
		for(int i=0;i<blockConts-maxBlockNumber;i++){
			begin = maxBlockNumber + i;
			String str = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+method+"\","+
					"\"params\""+":[\"0x"+Integer.toHexString(begin)+"\",true],"+"\"id\""+":"+87+"}";
		    JSONObject resultObj = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,str));
		    log.info("采集block,位置:"+begin+",结果：" + resultObj);
		    if(resultObj!=null && resultObj.getString("result") != null){
		    	JSONObject resultJson = JSONObject.parseObject(resultObj.getString("result"));
		    	String hash = resultJson.getString("hash");
		    	String number = String.valueOf(Long.parseLong(resultJson.getString("number").replaceAll("^0[x|X]", ""), 16));
		    	String timestamp = String.valueOf(Long.parseLong(resultJson.getString("timestamp").replaceAll("^0[x|X]", ""), 16));
		    	
		    	BlockInfo blockInfo = new BlockInfo();
		    	blockInfo.setId(begin+1);
		    	blockInfo.setHash(hash);
		    	blockInfo.setNumber(number);
		    	BlockInfo block = jfscMapper.getBlockByHash(hash);
		    	if(block==null){
		    		try {
						jfscMapper.insertBlock(blockInfo);
					} catch (Exception e) {
						e.printStackTrace();
						log.error("block入库失败，原因: " + e.getMessage());
					}
		    	}
		    	
		    	JSONArray transArray = JSONArray.parseArray(resultJson.getString("transactions"));
		    	for(int t=0;t<transArray.size();t++){
		    		JSONObject transObj = JSONObject.parseObject(transArray.getString(t));
		    		String blockNumber = String.valueOf(Long.parseLong(transObj.getString("blockNumber").replaceAll("^0[x|X]", ""), 16));
		    		String transactionIndex = String.valueOf(Long.parseLong(transObj.getString("transactionIndex").replaceAll("^0[x|X]", ""), 16));
		    		
		    		Transaction transaction = new Transaction();
		    		int t_id = jfscMapper.getMaxIdFromTransaction();
		    		transaction.setId(t_id);
		    		transaction.setBlockNumber(blockNumber);
		    		transaction.setHash(transObj.getString("hash"));
		    		transaction.setTransactionIndex(transactionIndex);
		    		transaction.setTimestamp(timestamp);
		    		transaction.setFromAccount(transObj.getString("from"));
		    		transaction.setToAccount(transObj.getString("to"));
		    		Transaction trans = jfscMapper.getTransByHash(transaction.getHash());
		    		if(trans==null){
			    		try {
		    				jfscMapper.insertTransaction(transaction);
						} catch (Exception e) {
							e.printStackTrace();
							log.error("block上的transaction入库失败，原因: " + e.getMessage());
						}
		    		}
		    	}
		    }
		}
		log.info("采集完成，开始位置："+maxBlockNumber+", 采集数量："+(blockConts-maxBlockNumber));
	}
	
	/**
	 * 查询交易信息,改用数据库查询,暂时废弃
	 * @param blockNumber 区块
	 * @param count 查询数量
	 * @return
	 */
	public List<Transaction> getTransactionInfoXXXXX(int start,int count){
		String blockMethod ="eth_getBlockByNumber";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		List<Transaction> list = new ArrayList<Transaction>();
		int begin = 0;
		for(int i=0;i<count;i++){
			begin = start + i;
			
			String blockStr = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+blockMethod+"\","+
					"\"params\""+":[\"0x"+Integer.toHexString(begin)+"\",true],"+"\"id\""+":"+87+"}";
		    JSONObject blockResult = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,blockStr));
		    JSONObject blockJson = JSONObject.parseObject(blockResult.getString("result"));
		    if(blockJson!=null){
		    	String timestamp = blockJson.getString("timestamp");
		    	JSONArray transArray = JSONArray.parseArray(blockJson.getString("transactions"));
		    	for(int k=0;k<transArray.size();k++){
				   JSONObject transObj = JSONObject.parseObject(transArray.getString(k));
				   String blockNumber = String.valueOf(Long.parseLong(transObj.getString("blockNumber").replaceAll("^0[x|X]", ""), 16));
				   String gas = String.valueOf(Long.parseLong(transObj.getString("gas").replaceAll("^0[x|X]", ""), 16));
				   String gasPrice = String.valueOf(Long.parseLong(transObj.getString("gasPrice").replaceAll("^0[x|X]", ""), 16));
				   String nonce = String.valueOf(Long.parseLong(transObj.getString("nonce").replaceAll("^0[x|X]", ""), 16));
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
				   transaction.setNonce(nonce);
				   transaction.setR(transObj.getString("r"));
				   transaction.setS(transObj.getString("s"));
				   transaction.setTo(transObj.getString("to"));
				   transaction.setTransactionIndex(transactionIndex);
				   transaction.setV(transObj.getString("v"));
				   transaction.setValue(value);
				   transaction.setTimestamp(timestamp);
				   list.add(transaction);
			   }
		    }
		}
	    return list;
	}
	
	public List<Transaction> getTransactionInfo(int start,int count,List<String> searchList){
		String transMethod ="eth_getTransactionByHash";
		String BASE_URI = ProConfig.getJfsc_eth_url();
		List<Transaction> list = new ArrayList<Transaction>();
		
		if(searchList==null){
			Query query = new Query();
			query.setStart(start);
			query.setCount(count);
			List<Transaction> transList = jfscMapper.getTransByIndex(query);
			for(Transaction transaction : transList){
				String transStr = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+transMethod+"\","+
						"\"params\""+":[\""+transaction.getHash()+"\"],"+"\"id\""+":"+87+"}";
			    JSONObject transResult = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,transStr));
			    putTransactionInfo(transResult,list,transaction);
			}
		}else{
			for(String hash : searchList){
				Transaction transaction = jfscMapper.getTransByHash(hash);
				String transStr = "{\"jsonrpc\""+":"+"\"2.0\""+","+"\"method\""+":\""+transMethod+"\","+
						"\"params\""+":[\""+hash+"\"],"+"\"id\""+":"+87+"}";
			    JSONObject transResult = JSONObject.parseObject(HttpClientUtil.httpPostEth(BASE_URI,transStr));
			    putTransactionInfo(transResult,list,transaction);
			}
		}
		
	    return list;
	}
	
	public void putTransactionInfo(JSONObject transResult,List<Transaction> list,Transaction transaction){
		JSONObject transObj = JSONObject.parseObject(transResult.getString("result"));
	    if(transObj!=null){
		   String blockNumber = String.valueOf(Long.parseLong(transObj.getString("blockNumber").replaceAll("^0[x|X]", ""), 16));
		   String gas = String.valueOf(Long.parseLong(transObj.getString("gas").replaceAll("^0[x|X]", ""), 16));
		   String gasPrice = String.valueOf(Long.parseLong(transObj.getString("gasPrice").replaceAll("^0[x|X]", ""), 16));
		   String nonce = String.valueOf(Long.parseLong(transObj.getString("nonce").replaceAll("^0[x|X]", ""), 16));
		   String transactionIndex = String.valueOf(Long.parseLong(transObj.getString("transactionIndex").replaceAll("^0[x|X]", ""), 16));
		   BigDecimal bb = new BigDecimal(new BigInteger(transObj.getString("value").replaceAll("^0[x|X]", ""), 16).doubleValue());
   		   String value = String.valueOf(bb.divide(Constants.BASE_COIN, BigDecimal.ROUND_HALF_DOWN));
		   transaction.setBlockHash(transObj.getString("blockHash"));
		   transaction.setBlockNumber(blockNumber);
		   transaction.setFrom(transObj.getString("from"));
		   transaction.setGas(gas);
		   transaction.setGasPrice(gasPrice);
		   transaction.setHash(transObj.getString("hash"));
		   transaction.setInput(transObj.getString("input"));
		   transaction.setNonce(nonce);
		   transaction.setR(transObj.getString("r"));
		   transaction.setS(transObj.getString("s"));
		   transaction.setTo(transObj.getString("to"));
		   transaction.setTransactionIndex(transactionIndex);
		   transaction.setV(transObj.getString("v"));
		   transaction.setValue(value);
		   transaction.setTimestamp(Utils.stampToDate(transaction.getTimestamp()));
		   list.add(transaction);
	    }
	}
	
	
	public List<Transaction> getTransByAccount(String account){
		return jfscMapper.getTransByAccount(account);
	}
	
}
