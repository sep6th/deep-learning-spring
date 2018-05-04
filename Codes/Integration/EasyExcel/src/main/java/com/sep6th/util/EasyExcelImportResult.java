package com.sep6th.util;

import java.util.List;

import lombok.Data;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 * 
 * Excel导入结果返回数据结构
 */

@Data
public class EasyExcelImportResult {
	/**
	 * excel记录数
	 */
	private Long dataCount;
	/**
	 * 不合法数据数量
	 */
	private Long errDataCount;
	/**
	 * 成功导入db数据数量
	 */
	private Long importDataCount;
	/**
	 * xml配置某些字段不能为空，实际Excel中却为空的集合
	 */
	List<String> xyOfNullList;
	/**
	 * xml配置某些字段数据类型，实际Excel中却为无法转为目标类型的集合
	 */
	List<String> xyOfTypeNoMatchList;
	/**
	 * xml配置某一字段唯一，实际Excel中该字段却重复的集合
	 */
	List<String> xyOfRepList;
	
	/**
	 * 构造函数
	 * @param dataCount
	 * @param errDataCount
	 * @param importDataCount
	 * @param xyOfNullList
	 * @param xyOfTypeNoMatchList
	 * @param xyOfRepList
	 */
	public EasyExcelImportResult(Long dataCount, Long errDataCount, Long importDataCount, List<String> xyOfNullList,
			List<String> xyOfTypeNoMatchList, List<String> xyOfRepList) {
		super();
		this.dataCount = dataCount;
		this.errDataCount = errDataCount;
		this.importDataCount = importDataCount;
		this.xyOfNullList = xyOfNullList;
		this.xyOfTypeNoMatchList = xyOfTypeNoMatchList;
		this.xyOfRepList = xyOfRepList;
	}
	
	/**
	 * 构建返回结果
	 */
	public static EasyExcelImportResult build(Long dataCount, Long errDataCount, Long importDataCount, List<String> xyOfNullList,
			List<String> xyOfTypeNoMatchList, List<String> xyOfRepList){
		return new EasyExcelImportResult(dataCount, errDataCount , importDataCount, xyOfNullList, xyOfTypeNoMatchList, xyOfRepList);
	}
	
}
