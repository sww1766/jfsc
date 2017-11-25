package com.service;



import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiWorkAdapter {
	
	private static final BlockingQueue<MultiWork> workQueue = new LinkedBlockingQueue<MultiWork>();
	
	public static final AtomicInteger ati = new AtomicInteger(0);
	
	public static void addQWork(MultiWork work){
		workQueue.offer(work);
	}
	
	public synchronized static void executeOne(){
		MultiWork work = workQueue.poll();
		if(null != work){
			work.executeWorks();
		}
	}
	
	public synchronized static boolean needWait(){
		return ati.get() >= 5;
	}
}
