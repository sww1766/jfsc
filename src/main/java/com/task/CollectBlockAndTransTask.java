package com.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.service.JfService;


/**
* @desc: 采集eth块和交易信息定时任务
*/
@Component
public class CollectBlockAndTransTask {
	public final static long ONE_TIME =  5 * 1000; //每5s延时执行一次
	
	@Autowired
	private JfService jfService;

//	@Scheduled(fixedDelay=ONE_TIME)
	public void collect() {
		jfService.collectInfo();
	}
}
