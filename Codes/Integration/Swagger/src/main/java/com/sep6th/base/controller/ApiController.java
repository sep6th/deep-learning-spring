package com.sep6th.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */
@Controller
public class ApiController {

	/**
	 * 显示API 文档
	 */
	@RequestMapping("/api")
	public String api(){
		return "api-docs";
	}
	
}
