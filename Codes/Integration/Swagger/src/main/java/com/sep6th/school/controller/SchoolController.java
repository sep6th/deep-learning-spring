package com.sep6th.school.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sep6th.school.pojo.School;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */
@RestController
@Api(value = "学校管理API")
@RequestMapping(value = "/school", produces = MediaType.APPLICATION_JSON_VALUE)
public class SchoolController {
	
private static Map<Long, School> schools = new ConcurrentHashMap<>();
	
    @ApiOperation(value = "获取学校列表", notes = "返回List<student>类型学校信息的JSON;")
    @GetMapping("/list")
    public List<School> getSchoolList() {
        return new ArrayList<>(schools.values());
    }

    @ApiOperation(value = "创建学校", notes = "根据school对象创建学校", response = School.class)
    @ApiParam(name = "school", value = "学校详细实体", required = true)
    @PostMapping("/school")
    public School postSchool(@RequestBody School school) {
        schools.put(school.getId(), school);
        return school;
    }
	
	
}
