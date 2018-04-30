package com.sep6th.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sep6th.service.IDemoService;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */

@RestController
public class HelloController {
	
	@Reference
	private IDemoService demoService;
	
	@RequestMapping("/hello")
	public String hello() {
		return demoService.hello("sep6th");
	}
	
}
