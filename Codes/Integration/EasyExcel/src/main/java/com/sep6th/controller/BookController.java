package com.sep6th.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sep6th.service.BookSrvImpl;
import com.sep6th.util.EasyExcelImportResult;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */

@Controller
@RequestMapping("/book")
public class BookController {

	@Autowired
	private BookSrvImpl bookSrv;
	
	/**
	 * 导出
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletResponse response) throws Exception {
		String fileName = "书籍清单";
		bookSrv.exportExcel(response,fileName);
	}
	
	
	/**
	 * 导入
	 * 返回的json格式：
	 * {"dataCount":3,"errDataCount":0,"importDataCount":3,"xyOfNullList":[],"xyOfTypeNoMatchList":[],"xyOfRepList":[]}
	 */
	@RequestMapping("/importExcel")
	@ResponseBody
	public EasyExcelImportResult importExcel(Model model, MultipartFile bookExcel){
		return bookSrv.importExcel(bookExcel);
	}
	
}
