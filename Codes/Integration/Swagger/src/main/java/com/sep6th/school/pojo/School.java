package com.sep6th.school.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */
@Data
@ApiModel(description = "学校描述信息")
public class School {

	@ApiModelProperty(value = "id",required = true)
    private Long id;
	
	@ApiModelProperty(value = "名称，比如'北京大学'")
    private String name;
	
}