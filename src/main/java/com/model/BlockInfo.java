package com.model;

import java.util.List;

public class BlockInfo {
	public int id;
	public String difficulty;
	public String extraData;
	public String gasLimit;
	public String gasUsed;
	public String hash;
	public String logsBloom;
	public String miner;
	public String mixHash;
	public String nonce;
	public String number;
	public String parentHash;
	public String receiptsRoot;
	public String sha3Uncles;
	public String size;
	public String stateRoot;
	public String timestamp;
	public String totalDifficulty;
	public List<Transaction> transactions;
	public String transactionsRoot;
	public List<String> uncles;
	public String transSize;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public String getGasLimit() {
		return gasLimit;
	}

	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}

	public String getGasUsed() {
		return gasUsed;
	}

	public void setGasUsed(String gasUsed) {
		this.gasUsed = gasUsed;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getLogsBloom() {
		return logsBloom;
	}

	public void setLogsBloom(String logsBloom) {
		this.logsBloom = logsBloom;
	}

	public String getMiner() {
		return miner;
	}

	public void setMiner(String miner) {
		this.miner = miner;
	}

	public String getMixHash() {
		return mixHash;
	}

	public void setMixHash(String mixHash) {
		this.mixHash = mixHash;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getParentHash() {
		return parentHash;
	}

	public void setParentHash(String parentHash) {
		this.parentHash = parentHash;
	}

	public String getReceiptsRoot() {
		return receiptsRoot;
	}

	public void setReceiptsRoot(String receiptsRoot) {
		this.receiptsRoot = receiptsRoot;
	}

	public String getSha3Uncles() {
		return sha3Uncles;
	}

	public void setSha3Uncles(String sha3Uncles) {
		this.sha3Uncles = sha3Uncles;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getStateRoot() {
		return stateRoot;
	}

	public void setStateRoot(String stateRoot) {
		this.stateRoot = stateRoot;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTotalDifficulty() {
		return totalDifficulty;
	}

	public void setTotalDifficulty(String totalDifficulty) {
		this.totalDifficulty = totalDifficulty;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getTransactionsRoot() {
		return transactionsRoot;
	}

	public void setTransactionsRoot(String transactionsRoot) {
		this.transactionsRoot = transactionsRoot;
	}

	public List<String> getUncles() {
		return uncles;
	}

	public void setUncles(List<String> uncles) {
		this.uncles = uncles;
	}

	public String getTransSize() {
		return transSize;
	}

	public void setTransSize(String transSize) {
		this.transSize = transSize;
	}

}
