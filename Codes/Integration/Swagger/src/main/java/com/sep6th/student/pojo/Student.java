package com.sep6th.student.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */
@Data
@ApiModel(description = "学生描述信息")
public class Student {

	@ApiModelProperty(value = "id",required = true)
    private Long id;
	
	@ApiModelProperty(value = "姓名，比如'李四'")
    private String name;
	
	@ApiModelProperty(value = "年龄，比如：20")
    private Integer age;
	
	@ApiModelProperty(value = "性别：1-男，0-女", allowableValues = "1,0")
	private Integer sex;
	
	
}
