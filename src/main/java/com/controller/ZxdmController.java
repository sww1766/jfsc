package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.util.HttpClientUtil;

@Controller
public class ZxdmController {
	
	@RequestMapping("/authZx")
    public @ResponseBody String authZx(HttpServletRequest request,Model model) {
    	String url = "http://10.85.202.13:4000/users";
    	String result = HttpClientUtil.doZxAuthPost(url);
        return result;
    }

	@RequestMapping("/queryZx")
    public @ResponseBody String queryZx(HttpServletRequest request,Model model) {
    	String url = "http://10.85.202.13:4000/query/table01/key01";
    	String result = HttpClientUtil.doZxGet(url);
        return result;
    }
	
	@RequestMapping("/insertZx")
    public @ResponseBody String insertZx(HttpServletRequest request,Model model) {
    	String url = "http://10.85.202.13:4000/insert/table01/key01";
    	String object = "{\"field1\":\"021\",\"field2\":\"123\",\"field3\":\"123\",\"field4\":\"\"}";
    	String result = HttpClientUtil.doZxPost(url,object);
        return result;
    }
	
	@RequestMapping("/batchInsertZx")
    public @ResponseBody String batchInsertZx(HttpServletRequest request,Model model) {
    	String url = "http://10.85.202.13:4000/batch/table01/";
    	String object = "[\n  {\n  \t\"key\":\"key03\",\n\t\"args\":{\"field1\":\"1\",\"field2\":\"123\",\"field3\":\"123\",\"field4\":\"7\"}\n  },\n  {\n  \t\"key\":\"key04\",\n\t\"args\":{\"field1\":\"2\",\"field2\":\"123\",\"field3\":\"123\",\"field4\":\"8\"}\n  }\n]";
    	String result = HttpClientUtil.doZxPost(url,object);
        return result;
    }
}
