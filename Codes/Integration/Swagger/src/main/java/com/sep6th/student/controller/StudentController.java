package com.sep6th.student.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sep6th.student.pojo.Student;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */
@RestController
@Api(value = "学生管理API")
@RequestMapping(value = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController {

	private static Map<Long, Student> students = new ConcurrentHashMap<>();
	
    @ApiOperation(value = "获取学生列表", notes = "返回List<student>类型学生信息的JSON;")
    @GetMapping("/list")
    public List<Student> getStudentList() {
        return new ArrayList<>(students.values());
    }

    @ApiOperation(value = "创建学生", notes = "根据student对象创建学生", response = Student.class)
    @ApiParam(name = "student", value = "学生详细实体", required = true)
    @PostMapping("/student")
    public Student postStudent(@RequestBody Student student) {
        students.put(student.getId(), student);
        return student;
    }

    @ApiOperation(value = "获取学生详细信息", notes = "根据url的id来获取学生详细信息")
    @ApiImplicitParam(name = "id", value = "学生ID", required = true, dataType = "Long")
    @ApiResponse(code = 200, message = "", response = Student.class)
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return students.get(id);
    }

    @ApiOperation(value = "更新学生详细信息", notes = "根据url的id来指定更新对象，并根据传过来的student信息来更新学生详细信息")
    @ApiImplicitParam(name = "id", value = "学生ID", required = true, dataType = "Long")
    @ApiParam(name = "student", value = "学生详细实体", required = true)
    @PutMapping("/{id}")
    public Student putStudent(@PathVariable Long id, @RequestBody Student student) {
        Student u = students.get(id);
        u.setName(student.getName());
        u.setAge(student.getAge());
        return students.put(id, u);
    }

    @ApiOperation(value = "删除学生", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "学生ID", required = true, dataType = "Long")
    @DeleteMapping("/{id}")
    public Student deleteStudent(@PathVariable Long id) {
        return students.remove(id);
    }
	
}
