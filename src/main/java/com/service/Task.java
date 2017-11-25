package com.service;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Task extends Thread{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	public Task(CountDownLatch beg,CountDownLatch end){
		this.beg = beg;
		this.end = end;
	}
	
	public Task(){
		
	}
	
	private CountDownLatch beg;
	
	private CountDownLatch end;
	
	public CountDownLatch getBeg() {
		return beg;
	}

	public void setBeg(CountDownLatch beg) {
		this.beg = beg;
	}

	public CountDownLatch getEnd() {
		return end;
	}

	public void setEnd(CountDownLatch end) {
		this.end = end;
	}

	public void run() {
		try {
			beg.await();
			callback();
		} catch (Exception e) {
			log.error(e.fillInStackTrace());//chencen REQLDT-161121-922 - 团险承保系统代码缺陷调整
		}finally{
			end.countDown();
		}
	}
	
	public abstract void callback();
	
}
