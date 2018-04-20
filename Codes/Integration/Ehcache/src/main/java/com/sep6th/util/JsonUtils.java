package com.sep6th.util;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */
public class JsonUtils {

    // 定义[jackson]对象
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    /**
     * [jackson]
     * map OR pojo -> json字符串。
     */
    public static String toJson(Object obj) {
    	try {
			String string = MAPPER.writeValueAsString(obj);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * [jackson]
     * json字符串 -> 对象
     * 
     */
    public static <T> T jsonToPojo(String jsonStr, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonStr, beanType);
            return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * [jackson]
     * json字符串 -> List
     */
    public static <T>List<T> jsonToList(String jsonStr, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonStr, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    
    /**  =================  黄金分割线  =================  **/
    
    /**
     * [fastJSON]
	 * 对象 -> json字符串
	 */
	public static String toJsonFast(Object object){
		return JSON.toJSONString(object);
	}
    
    /**
     * [fastJSON]
	 * json字符串 -> 对象
	 */
	public static <T> T parseObject(String jsonStr, Class<T> beanType){
		return JSON.parseObject(jsonStr, beanType);
	}
    
    /**
     * [fastJSON]
	 * json字符串 -> List
	 */
	public static <T> List<T> parsetArray(String jsonStr, Class<T> beanType){
		return JSON.parseArray(jsonStr, beanType);
	}
	
	/**  =================  黄金分割线  =================  **/
	
	/**
	 * 
	 * [org.json]
	 * map OR pojo -> json字符串
	 */
	public static String objectToJson(Object object){
		return new JSONObject().toString();
	}
	
	/**
	 * 
	 * [org.json]
	 * list<pojo> -> json字符串
	 */
	public static String listToJson(List<Object> list){
		return new JSONArray(list).toString();
	}
	
	/**
	 * 
	 * [org.json]
	 * list<map> -> json字符串
	 */
	public static String listMapToJson(List<Map<String, Object>> list){
		JSONArray arrMapList = new JSONArray();
		for(int i = 0; i < list.size(); i++){                   
	        arrMapList.put(i, list.get(i));//有序排列适合经SQL[order by]查出的数据                 
	        //arrMapList.put(lm.get(i));//可能无序              
	    }
		return arrMapList.toString();
	}
	
    
}