package com.service;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mapper.ZxStageMapper;
import com.model.ZxTable;
import com.util.ZxTableConfig;

@Service
public class ZxStageService{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ZxStageMapper zxStageMapper;
	
	//每次上传条数
	private int number = 50;
	
	@SuppressWarnings({"unused"})
	public String sendData(int begin,int count){
		for(int k=0; k<27; k++){
			String tableName = "";
			switch (k) {
				case 0:
					tableName = ZxTableConfig.get_1();
					break;
				case 1:
					tableName = ZxTableConfig.get_2();
					break;
				case 2:
					tableName = ZxTableConfig.get_3();
					break;
				case 3:
					tableName = ZxTableConfig.get_4();
					break;
				case 4:
					tableName = ZxTableConfig.get_5();
					break;
				case 5:
					tableName = ZxTableConfig.get_6();
					break;
				case 6:
					tableName = ZxTableConfig.get_7();
					break;
				case 7:
					tableName = ZxTableConfig.get_8();
					break;
				case 8:
					tableName = ZxTableConfig.get_9();
					break;
				case 9:
					tableName = ZxTableConfig.get_10();
					break;
				case 10:
					tableName = ZxTableConfig.get_11();
					break;
				case 11:
					tableName = ZxTableConfig.get_12();
					break;
				case 12:
					tableName = ZxTableConfig.get_13();
					break;
				case 13:
					tableName = ZxTableConfig.get_14();
					break;
				case 14:
					tableName = ZxTableConfig.get_15();
					break;
				case 15:
					tableName = ZxTableConfig.get_16();
					break;
				case 16:
					tableName = ZxTableConfig.get_17();
					break;
				case 17:
					tableName = ZxTableConfig.get_18();
					break;
				case 18:
					tableName = ZxTableConfig.get_19();
					break;
				case 19:
					tableName = ZxTableConfig.get_20();
					break;
				case 20:
					tableName = ZxTableConfig.get_21();
					break;
				case 21:
					tableName = ZxTableConfig.get_22();
					break;
				case 22:
					tableName = ZxTableConfig.get_23();
					break;
				case 23:
					tableName = ZxTableConfig.get_24();
					break;
				case 24:
					tableName = ZxTableConfig.get_25();
					break;
				case 25:
					tableName = ZxTableConfig.get_26();
					break;
				case 26:
					tableName = ZxTableConfig.get_27();
					break;
				default:
					break;
			}
			
			ZxTable zxTable = new ZxTable();
			zxTable.setTableName(tableName);
			zxTable.setBegin(begin);
			zxTable.setCount(count);
			//查询表
			List<Map<String,Object>> list = zxStageMapper.queryForList(zxTable);
			int y=0;
			JSONArray jsonArray = new JSONArray();
			for(int j=0;j<list.size();j++){
				Map<String,Object> t = (Map<String, Object>) list.get(j);
				jsonArray.add(t);
		        if(y==number){
		        	y=0;
		        	//改动3：链接后面录入的表改一下
		        	//String url="http://10.85.202.26:4000/insert/bc_seedling_strengthen_agent/"+ t.getId();bc_planting_plots
		        	String url="http://101.132.64.104:4000/batch/"+tableName+"/";
					HttpPost post = new HttpPost(url);
				    post.setHeader("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjUxMDUzODA3NjgsInVzZXJuYW1lIjoiaGVsbG8iLCJvcmdOYW1lIjoib3JnMSIsImlhdCI6MTUwNTM4MDc2OH0.CF_jQVxHpJ9GlQGzUL37qEn4FKKKKJuEnJNfb6V9LWc");
				    post.setHeader("content-type", "application/json");
					
				    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
			        JSONObject response = null;
			        try {
			        	//httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,100000);//设置连接时间
			            StringEntity s = new StringEntity(jsonArray.toJSONString());
			          //s.setContentEncoding("UTF-8");
			            s.setContentType("application/json");//发送json数据需要设置contentType
			            post.setEntity(s);
			            HttpResponse res = httpclient.execute(post);
			            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
			                logger.info("第"+j+"条结果：" +result);
			            }
			        } catch (Exception e) {
			        	logger.error("调上链接口失败：" + e.getMessage());
			        }
			        
			        jsonArray.clear();
		        }
		        y++;
			}
					
			logger.info("开始："+begin+",数量："+count+",第 "+k+" 张表结束");
		}
		return "success,sendData finished";
	}
	
	@SuppressWarnings({"rawtypes","unchecked"})
	public String test(int begin,int count){
		ZxTable zxTable = new ZxTable();
		zxTable.setTableName("productorder");
		zxTable.setBegin(begin);
		zxTable.setCount(count);
		List<Map<String,Object>> list = zxStageMapper.queryForList(zxTable);
		
		List<List> l = MultiWork.getSize(list.size(), 110000, list);
		MultiWork m = new MultiWork(l.size());
		for(final List s : l){
			m.addWork(new Task(m.getBegin(),m.getEnd()){

				@Override
				public void callback() {
					int y=0;
					JSONArray jsonArray = new JSONArray();
					for(int i=0;i<s.size();i++){
						Map<String,Object> t = (Map<String, Object>) s.get(i);
						JSONObject json = new JSONObject();
						json.put("id", t.get("id"));
						String js = JSONObject.toJSONString(t);
						json.put("args", js);
						jsonArray.add(json);
				        if(y==10){
				        	y=0;
				            System.out.println(jsonArray.toJSONString());
				        }
				        y++;
					}
				}
			});
		}
		m.executeWorks();
		logger.info("<<<<<<<<<<<<<<<<<<结束");
		return "success,sendData finished";
	}
}
