package com.sep6th.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sep6th.pojo.User;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 * 该类用于模拟数据库CURD数据
 */
public class DataFromDB {

	private static Logger logger = LoggerFactory.getLogger(DataFromDB.class);
	
	private static List<User> list = new ArrayList<User>();
	
	/**
	 * 增
	 */
	public static void insertUser(User user){
		logger.info("insertUser("+user.toString()+")====添加user");
		list.add(user);
	}
	
	public static void insertListUser(List<User> lu){
		logger.info("insertUser(List<User>)====添加List<User>");
		for(User user : lu){
			list.add(user);
		}
	}
	
	
	/**
	 * 删
	 */
	public static void delUserById(Long id){
		logger.info("delUserById("+id+")====删除user");
		for(User user : list){
			if(user.getId() == id){
				list.remove(user);
				break;
			}
		}
	}
	
	/**
	 * 改
	 */
	public static void updateUser(User user){
		logger.info("updateUser("+user.toString()+")====更新user");
		for(User temp : list){
			if(temp.getId() == user.getId()){
				list.remove(temp);
				list.add(user);
				break;
			}
		}
	}
	
	/**
	 * 查
	 */
	public static List<User> selectUserList(){
		logger.info("getUserList====从数据库取数据啦！没有从cache中取哦！！！");
		return list;
	}
	
}
