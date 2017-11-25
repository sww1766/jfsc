package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.ZxStageService;


@Controller
public class ZxStageController {
	
	@Autowired
	private ZxStageService zxStageService;

	@RequestMapping("/sendData")
    public @ResponseBody String sendData(HttpServletRequest request) {
		int begin = 0;
		int count = 0;
		try {
			begin = Integer.parseInt(request.getParameter("begin")==null?"0":request.getParameter("begin"));
			count = Integer.parseInt(request.getParameter("count")==null?"0":request.getParameter("count"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "begin和count必须都是正整数";
		}
		
		if(count==0){
			return "count必须大于0";
		}
		return zxStageService.sendData(begin,count);
    }
}
