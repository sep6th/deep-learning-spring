package com.sep6th.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.sep6th.service.IDemoService;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */
@Service
public class DemoService implements IDemoService {

	@Override
	public String hello(String name) {
		return "Hello "+name;
	}

}
