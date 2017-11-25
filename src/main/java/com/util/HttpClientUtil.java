package com.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil {
	static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	
	public static String doPost(String url, Map<String, String> param) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}

				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}

			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}
	
	public static String doGet(String url){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("content-type", "application/json");
			Map<String, String> param = new HashMap<String, String>();
			param.put("username", "Jim");
			param.put("orgName", "org1");
			String auth = doPost(ProConfig.getToken_url(),param);
			JSONObject authObj = JSONObject.parseObject(auth);
			String token = authObj.getString("token");
			httpGet.addHeader("authorization", "Bearer "+token);
			response = httpClient.execute(httpGet);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}
	
	
	public static String createFp(String url,String jsonstr) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("content-type", "application/json");
			Map<String, String> param1 = new HashMap<String, String>();
			param1.put("username", "Jim");
			param1.put("orgName", "org1");
			String auth = doPost(ProConfig.getToken_url(),param1);
			JSONObject authObj = JSONObject.parseObject(auth);
			String token = authObj.getString("token");
			httpPost.addHeader("authorization", "Bearer "+token);
			StringEntity se = new StringEntity(jsonstr);
			se.setContentType("text/json");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}
	
	
	public static String doZxGet(String url){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("content-type", "application/json");
			httpGet.addHeader("authorization", "Bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjUxMDMzOTc2ODEsInVzZXJuYW1lIjoiSmltIiwib3JnTmFtZSI6Im9yZzEiLCJpYXQiOjE1MDMzOTc2ODF9.3z7AM7QZSKIlPV4ZGx6Yh7pt6oY2Qj13WrNtwTaEzag");
			response = httpClient.execute(httpGet);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}
	
	public static String doZxPost(String url, String jsonstr) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("content-type", "application/json");
			httpPost.addHeader("authorization", "Bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjUxMDMzOTc2ODEsInVzZXJuYW1lIjoiSmltIiwib3JnTmFtZSI6Im9yZzEiLCJpYXQiOjE1MDMzOTc2ODF9.3z7AM7QZSKIlPV4ZGx6Yh7pt6oY2Qj13WrNtwTaEzag");
			StringEntity se = new StringEntity(jsonstr);
			se.setContentType("text/json");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}
	
	public static String doZxAuthPost(String url) {
		Map<String, String> param1 = new HashMap<String, String>();
		param1.put("username", "Jim");
		String auth = doPost(url,param1);
		JSONObject authObj = JSONObject.parseObject(auth);
		String token = authObj.getString("token");
		return token;
	}
	
	public static String httpPostEth(String url, String postdata) {
		String body = null;
		try {
			// post请求
			HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Content-Type", "application/json");
			httppost.addHeader("cache-control", "no-cache");
			httppost.addHeader("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0OTQ0NzcyODMsInVzZXJuYW1lIjoiYWRtaW4iLCJvcmdOYW1lIjoib3JnMSIsImlhdCI6MTQ5NDQ3MzY4M30.0bgdOYJAFeaL7vHt1ttX9TnxgCM_m2jX8w9Wk0HCOsA");
			httppost.addHeader("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0OTQ0NzcyODMsInVzZXJuYW1lIjoiYWRtaW4iLCJvcmdOYW1lIjoib3JnMSIsImlhdCI6MTQ5NDQ3MzY4M30.0bgdOYJAFeaL7vHt1ttX9TnxgCM_m2jX8w9Wk0HCOsA");
			
			// 设置参数
			StringEntity entity = new StringEntity(postdata);
            entity.setContentEncoding( "UTF-8" );
            entity.setContentType( "application/json" );//设置为 json数据
            
            httppost.setEntity(entity);
            HttpClient client = HttpClients. createDefault();
            HttpResponse httpresponse =client.execute(httppost);
            
            HttpEntity entity1 = httpresponse.getEntity();

            body = EntityUtils.toString(entity1);
            
            EntityUtils.consume(entity);
			
			httppost.releaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用eth失败,原因：" + e.getMessage());
		}
		
		return body;
	}
	
}
