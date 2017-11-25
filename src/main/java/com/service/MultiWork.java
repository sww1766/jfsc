package com.service;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiWork {
	
	public static final Lock lock = new ReentrantLock();
	
	public List<String> exists = new ArrayList<String>();
	
	public MultiWork(){
		
	}
	
	public MultiWork(int size){
		setEnd(size);
	}
	
	public List<Runnable> workList = new ArrayList<Runnable>();
	
	private CountDownLatch begin = new CountDownLatch(1);

	public CountDownLatch getBegin() {
		return begin;
	}

	private CountDownLatch end = null;
	
	public CountDownLatch getEnd() {
		return end;
	}

	public void setEnd(int size) {
		this.end = new CountDownLatch(size);
	}

	public void addWork(Runnable r){
		workList.add(r);
	}
	
	public void executeWorks(){
		try {
			if(MultiWorkAdapter.needWait()){//超过5个 也就是100个连接数
				MultiWorkAdapter.addQWork(this);
			}else{
				executeInternal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void executeInternal(){
		try {
			MultiWorkAdapter.ati.incrementAndGet();
			ExecutorService pool = Executors.newFixedThreadPool(20);
			for(Runnable r : workList){
				pool.submit(r);
			}
			begin.countDown();
			try {
				end.await();
			} catch (InterruptedException e) {
				
			}
			pool.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			MultiWorkAdapter.ati.decrementAndGet();
			MultiWorkAdapter.executeOne();
		}
	}
	
	public static synchronized List<List> getSize(Integer i,Integer split,List list){
    	List<List> l = new ArrayList<List>();
    	try {
    		lock.lock();
    		int a = 0;
        	int j = split;
        	while(i > split){
        		List lr = new ArrayList();
        		for(;a<j;a++){
        			lr.add(list.get(a));
        		}
        		i = i - split;
        		j = j + split;
        		l.add(lr);
        	}
        	if(i > 0){
        		List lr = new ArrayList();
        		for(;a<list.size();a++){
        			lr.add(list.get(a));
        		}
        		l.add(lr);
        	}
		}finally{
			lock.unlock();
		}
    	return l;
    }
	
	public synchronized String getSequence(){
		String str = new BigDecimal(java.lang.Math.random()*100000).intValue()+"";
		while(str.length() < 6){
			str = new BigDecimal(java.lang.Math.random()*10).intValue()+""+str;
		}
		if(exists.contains(str)){
			return getSequence();
		}else{
			exists.add(str);
		}
		return str;
	}
	
}

