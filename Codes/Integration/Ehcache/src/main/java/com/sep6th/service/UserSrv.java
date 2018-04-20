package com.sep6th.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sep6th.data.DataFromDB;
import com.sep6th.pojo.User;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 * 省略接口IUserSrv
 */
@Service
public class UserSrv {
	

	/**
	 * 增
	 * @CachEvict 主要针对方法配置，能够根据一定的条件对缓存进行清空。
	 */
	@CacheEvict(value="UserList",key="'UserListKey'")  
	public void saveUser(User user){
		DataFromDB.insertUser(user);
	}
	
	@CacheEvict(value="UserList",key="'UserListKey'")  
	public void saveListUser(List<User> list){
		DataFromDB.insertListUser(list);
	}
	
	
	
	/**
	 * 删
	 */
	@CacheEvict(value="UserList",key="'UserListKey'") 
	public void removeUserById(Long id){
		DataFromDB.delUserById(id);
	}
	
	/**
	 * 改
	 */
	@CacheEvict(value="UserList",key="'UserListKey'") 
	public void modifyUser(User user){
		DataFromDB.updateUser(user);
	}
	
	/**
	 * 查
	 * @Cacheable 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存。
	 */
	@Cacheable(value="UserList",key="'UserListKey'")
	public List<User> queryList(){
		return DataFromDB.selectUserList();
	}
	
}
