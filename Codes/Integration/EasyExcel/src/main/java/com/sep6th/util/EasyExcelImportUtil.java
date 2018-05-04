package com.sep6th.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 * 
 * 导入Excel数据,并返回异常数据的位置，即单元格, 例：A3
 * 返回A3的用意：让用户在Excel左上角坐标输入框里，输入A3，快速定位到异常数据。
 */

public class EasyExcelImportUtil {

    /**
     * 方法描述：  读取Excel, 返回有问题的数据位置。
     * @param: excelFile   org.springframework.web.multipart.MultipartFile 实例.
     * @param: xmlSheetIndex  xml配置中模板sheet的序号, 从0开始.
     * @param: dataList  传入空的list, 去取合法数据.
     * @param: uniqueIdSet  从数据库查出唯一标识set集合 , 传入.
     */
    static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    public static EasyExcelImportResult readExcel(MultipartFile excelFile, Integer xmlSheetIndex, List<List<Object>> dataList, Set<String> uniqueIdSet) {
        // 同时支持 Excel 2003、2007、2010
        Workbook workbook;
        // 存储导入 Excel 模板
        List<String> xlsColumnList = new ArrayList<String>();
        // 存储xml模板（列名）
        List<String> xmlColumnNameList = new ArrayList<String>();
        // 存储每列数据的类型
        List<String> xmlColumnDataTypeList = new ArrayList<String>();
        // 存储数据中必填项为空的单元格坐标
        List<String> xyOfNullList = new ArrayList<String>();
        // 存储数据中与数据库唯一标识重复的数据唯一标识的单元格坐标
        List<String> xyOfRepList = new ArrayList<String>();
        // 存储数据中类型不匹配的数据所在单元格坐标
        List<String> xyOfTypeNoMatchList = new ArrayList<String>();
        // 存储xml必填项
        boolean[] xmlColumnNotNullArr;
        // 存储xml 列名对应的ABC...
        char[] xslEngCodeArr;
        // 记录唯一标识列是第几列
        Integer xmlColumnUnique = null;
        try {
            workbook = WorkbookFactory.create(excelFile.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取失败！");
        }
        // Sheet 的数量
        int sheetCount = workbook.getNumberOfSheets();
        Long rowCount = 3L;
        // 遍历每个 Sheet
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            // 获取总行数
            rowCount = (long) sheet.getPhysicalNumberOfRows();
            //获取 excel 标题行对象
            Row rowOfTitle = sheet.getRow(2);
            
            // 获取总列数
            int cellCount = rowOfTitle.getPhysicalNumberOfCells();
            // 获取导入 excel 标题
            xlsColumnList.clear();
            for (int j = 0; j < cellCount; j++) {
                xlsColumnList.add(rowOfTitle.getCell(j).getStringCellValue());
            }
            
            // xml 列名个数（即 总列数）
            Integer columnCount = EasyExcelHalper.getColumns(xmlSheetIndex).getChildrenCount("column");
            if (columnCount == null || "".equals(columnCount)) {
                throw new RuntimeException("获取不到xml中[sheet]序号为" + xmlSheetIndex + ",[column]的个数！");
            }
            // 必填项数组  实例化
            xmlColumnNotNullArr = new boolean[columnCount];
            xslEngCodeArr = new char[columnCount];
            // 获取 xml column 信息
            for (int j = 0; j < columnCount; j++) {
                if (xmlColumnNameList.size() < columnCount) {
                    
                    // 获取标题
                    String columnName = EasyExcelHalper.getXmlStringValue("sheets.sheet(" + xmlSheetIndex + ").columns.column(" + j + ").name");
                    if (columnName == null || "".equals(columnName)) {
                        throw new RuntimeException("获取不到xml中[sheet]序号为" + xmlSheetIndex + ",[column]序号为" + j + "[name]的值！");
                    }
                    xmlColumnNameList.add(columnName);
                    
                    // 获取必填列
                    String notNull = EasyExcelHalper.getXmlStringValue("sheets.sheet(" + xmlSheetIndex + ").columns.column(" + j + ").notNull");
                    if ("true".equals(notNull)) {
                        xmlColumnNotNullArr[j] = true;
                    } else {
                        xmlColumnNotNullArr[j] = false;
                    }
                    
                    // 获取唯一标识列是第几列
                    String unique = EasyExcelHalper.getXmlStringValue("sheets.sheet(" + xmlSheetIndex + ").columns.column(" + j + ").unique");
                    if ("true".equals(unique)) {
                        xmlColumnUnique = j;
                    }
                    
                    // 获取每列数据类型
                    String dataType = EasyExcelHalper.getXmlStringValue("sheets.sheet(" + xmlSheetIndex + ").columns.column(" + j + ").type");
                    if (dataType == null || "".equals(dataType)) {
                        throw new RuntimeException("获取不到xml中[sheet]序号为" + xmlSheetIndex + ",[column]序号为" + j + "[type]的值！");
                    }
                    xmlColumnDataTypeList.add(dataType);
                    
                    // 获取xml 列名对应的ABC...
                    xslEngCodeArr[j] = (char)(j + (int)'A');
                    
                }
            }
            if (!compare(xlsColumnList, xmlColumnNameList)) {
                throw new RuntimeException("模板不匹配！请检查模板是否正确。");
            }
            
            // 获取数据总行数
            if (rowCount - 3 <= 0) {
                continue;
            }
            // 从数据行开始，遍历每一行
            for (int r = 3; r < rowCount; r++) {
                Row row = sheet.getRow(r);
                List<Object> cellValue = new ArrayList<Object>();
                // 遍历每一列,列数以标题行为准
                for (int c = 0; c < cellCount; c++) {
                    Cell cell = row.getCell(c);
                    
                    // 默认单元格的类型为空
                    @SuppressWarnings({ "unused" })
                    int cellType = Cell.CELL_TYPE_BLANK;
                    if (cell!=null) {
                        cellType = cell.getCellType();
                    
                        switch (cell.getCellType()) {
                             // 文本
                            case Cell.CELL_TYPE_STRING:
                                cellValue.add(cell.getStringCellValue());
                                break;
                             // 数字、日期
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    try {
                                        // 日期型
                                        cellValue.add(fmt.format(cell.getDateCellValue()));
                                    } catch (Exception e) {
                                        cellValue.add(null);
                                    }
                                } else {
                                    Double data = cell.getNumericCellValue();
                                    if (data == null || "".equals(data)) {
                                        cellValue.add(0);
                                    } else {
                                        // 数字
                                        cellValue.add(String.valueOf(new DecimalFormat("#.##").format(cell.getNumericCellValue())));
                                    }
                                }
                                break;
                             // 布尔型
                            case Cell.CELL_TYPE_BOOLEAN:
                                cellValue.add(String.valueOf(cell.getBooleanCellValue()));
                                break;
                             // 公式
                            case Cell.CELL_TYPE_FORMULA:
                                cellValue.add(null);
                                break;
                             // 空白
                            case Cell.CELL_TYPE_BLANK:
                                cellValue.add(null);
                                break;
                             // 错误
                            case Cell.CELL_TYPE_ERROR:
                                cellValue.add(null);
                                break;
                            default:
                                cellValue.add(null);
                        }
                    }else {
                    	cellValue.add(null);
                    }
                    
                }
                // 用于判断是否是存进合法数据的 list 里
                boolean flag = true;
                // 获取了一条数据
                if (!cellValue.isEmpty() && cellValue.size() == cellCount) {
                    for (int k = 0; k < cellCount; k++) {
                        // 启动必填项校验
                        if (xmlColumnNotNullArr[k]) {
                            // 记录为空的单元格
                            if (cellValue.get(k) == null || "".equals(cellValue.get(k))) {
                                xyOfNullList.add(xslEngCodeArr[k] + "" + (r + 1));
                                flag = false;
                            }
                        }
                    }
                    // 启动唯一校验
                    if (xmlColumnUnique != null) {
                        Object object = cellValue.get(xmlColumnUnique);
                        if (object != null) {
                            // 利用set存不重复的值的特性
                            int size = uniqueIdSet.size();
                            uniqueIdSet.add((String)object);
                            if(uniqueIdSet.size() == size){
                                //与数据库唯一标识重复，不存数据！记录单元格
                                xyOfRepList.add(xslEngCodeArr[xmlColumnUnique] + "" + (r + 1));
                                flag = false;
                            }
                        }
                    }
                    // 数据类型匹配校验
                    for (int k = 0; k < cellCount; k++) {
                        String dataType = xmlColumnDataTypeList.get(k);
                        Object obj = cellValue.get(k);
                        if (obj != null){
                            if (!dataTypeMatch(dataType, obj)) {
                                xyOfTypeNoMatchList.add(xslEngCodeArr[k] + "" + (r + 1));
                            }
                        }
                    }
                }
                // 存储没有问题的数据
                if (flag) {
                    dataList.add(cellValue);
                }
            }
            
        }
        
        return EasyExcelImportResult.build(rowCount-3, rowCount-3-dataList.size(),
        			0L, xyOfNullList, xyOfTypeNoMatchList, xyOfRepList);
        
    }
    
    
    /**
     * 方法描述：  比较两个list存储的元素、个数及顺序是否一致,一致返回 true
     */
    private static boolean compare (List<String> a,List<String> b) {
        int size = a.size();
        if (size != b.size()) {
            return false;
        }
        for (int i = 0; i < size; i++ ) {
           if (!a.get(i).equals(b.get(i))) {
               return false;
           }
        }
        return true;
    }
    
    /**
     * 方法描述：  数据类型匹配    匹配返回true
     */
    private static boolean dataTypeMatch (String dataType, Object data) {
        boolean flag = false;
        switch (dataType) {
        case "java.lang.String":
            if (data instanceof String) {
                flag = true;
            }
            break;
        case "java.lang.Double":
        	try {
        		Double.parseDouble((String) data);
        		flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
            break;
        case "java.lang.Float":
        	try {
        		Float.parseFloat((String) data);
        		flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
            break;
        case "java.lang.Integer":
        	try {
        		Integer.parseInt((String) data);
        		flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
            break;
        case "java.lang.Long":
        	try {
        		Long.parseLong((String) data);
        		flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
            break;
        case "java.lang.Boolean":
        	try {
        		Boolean.parseBoolean((String) data);
        		flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
            break;
        case "java.util.Date":
            if (data instanceof String) {
                String time = (String)data;
                if(!"".equals(time) && time != null){
                    try {
                        fmt.parse(time);
                        flag = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        default:
            throw new RuntimeException("数据类型不支持！");
        }
        return flag;
    }
    
}
