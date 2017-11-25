package com.mapper;

import java.util.List;

import com.model.BlockInfo;
import com.model.Query;
import com.model.Transaction;

public interface JfscMapper {
	public int getMaxIdFromTransaction();
	public void insertTransaction(Transaction transaction);
	public Transaction getTransByHash(String hash);
	public List<Transaction> getTransByAccount(String account);
	public List<Transaction> getTransByIndex(Query query);
	public int getMaxIdFromBlock();
	public void insertBlock(BlockInfo blockInfo);
	public BlockInfo getBlockByHash(String hash);
	public List<BlockInfo> getBlockByIndex(Query query);
	public int getTransactionCounts();
	public int getBlockCounts();
}
