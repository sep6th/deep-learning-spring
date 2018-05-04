package com.sep6th.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.tree.ConfigurationNode;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 * 
 * EasyExcel助手, 加载Excel xml配置,及读取 xml
 */

public class EasyExcelHalper {

    /* 加载Excel xml配置   began */
    
    public static final String xmlFileName = "easyExcel-config.xml";
    public static XMLConfiguration cfg = null;
    static {
        try {
            cfg = new XMLConfiguration(xmlFileName);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        // 配置文件 发生变化就重新加载
        cfg.setReloadingStrategy(new FileChangedReloadingStrategy());
    }
    
    /* 加载Excel xml配置   end */
    
    
    /* xml 操作   began */
    
    private static Node root = cfg.getRoot();
        
    //获取 sheet 节点
    public static ConfigurationNode getSheet(Integer sheetCode){
        sheetCode = sheetCode == null ? 0 : sheetCode;
        return root.getChildren("sheets").get(0).getChildren("sheet").get(sheetCode);
    }
    
    //获取第 sheetCode 个 sheet 的 columns节点
    public static ConfigurationNode getColumns(Integer sheetCode){
        return getSheet(sheetCode).getChildren("columns").get(0);
    }
    
    //获取
    public static String getXmlStringValue(String key) {
        return cfg.getString(key);
    }
    
    /* xml 操作   end */
    
    
}
