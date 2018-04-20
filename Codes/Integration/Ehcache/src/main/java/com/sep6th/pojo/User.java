package com.sep6th.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */
@Data
@AllArgsConstructor //全参构造函数
@NoArgsConstructor	//无参构造函数
public class User implements Serializable {

	private static final long serialVersionUID = 6757055584659150681L;
	
	private Long id;
	
	private String name;
	

}
