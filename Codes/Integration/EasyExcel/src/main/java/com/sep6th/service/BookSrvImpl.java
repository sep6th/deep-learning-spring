package com.sep6th.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sep6th.util.EasyExcelExportUtil;
import com.sep6th.util.EasyExcelImportResult;
import com.sep6th.util.EasyExcelImportUtil;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 */

@Service
public class BookSrvImpl {

	/**
	 * 导出模板或数据
	 */
	public void exportExcel(HttpServletResponse response, String fileName) {
		
		Map<Integer,List<Object[]>> map = new HashMap<Integer,List<Object[]>>();
		map.put(0, null);
		try {
			EasyExcelExportUtil.export(fileName, map, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 导入数据
	 * @param bookExcel 
	 */
	public EasyExcelImportResult importExcel(MultipartFile bookExcel) {
		
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		
		// 模拟数据库查出的唯一标识列
		Set<String> uniqueIdSet = new HashSet<String>();
		uniqueIdSet.add("11");
		uniqueIdSet.add("22");
		uniqueIdSet.add("33");
		
		EasyExcelImportResult result= EasyExcelImportUtil.readExcel(bookExcel, 0, dataList, uniqueIdSet);
		
		if(result.getErrDataCount()==0){
			System.out.println("模拟取数据，向数据库添加数据中...");
			for (List<Object> list: dataList){
				for (int i = 0; i < list.size(); i++) {
					System.out.print(list.get(i)+"  ");
				}
				System.out.println();
			}
			result.setImportDataCount((long) dataList.size());
		}
		return result;
	}
	
}
