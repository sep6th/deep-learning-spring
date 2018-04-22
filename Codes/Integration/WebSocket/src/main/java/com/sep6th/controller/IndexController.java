package com.sep6th.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */
@Controller
public class IndexController {

	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	
}
