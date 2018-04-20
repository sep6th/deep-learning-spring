package com.sep6th.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sep6th.pojo.User;
import com.sep6th.service.UserSrv;
import com.sep6th.util.JsonUtils;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 * 
 * 使用postman进行测试,也可以使用在service里使用main测试
 * 哎！就想舍近求远，再熟悉熟悉postman
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserSrv userSrv;
	
	
	@PostMapping("/add")
	public void add(@RequestBody String parem){
		User user = JsonUtils.parseObject(parem, User.class);
		userSrv.saveUser(user);
		// 更新缓存
		userSrv.queryList();
	}
	
	@PostMapping("/addList")
	public void addList(@RequestBody String parem){
		List<User> list = JsonUtils.parsetArray(parem, User.class);
		userSrv.saveListUser(list);
		// 更新缓存
		userSrv.queryList();
	}
	
	@DeleteMapping("/del")
	public void del(@RequestBody String parem){
		User user = JsonUtils.parseObject(parem, User.class);
		userSrv.removeUserById(user.getId());
		// 更新缓存
		userSrv.queryList();
	}
	
	@PutMapping("/update")
	public void update(@RequestBody String parem){
		User user = JsonUtils.parseObject(parem, User.class);
		userSrv.modifyUser(user);
		// 更新缓存
		userSrv.queryList();
	}
	
	@GetMapping("/list")
	public List<User> list(){
		return userSrv.queryList();
	}
	
}
