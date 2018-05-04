## EasyExcel

EasyExcel 由五部分组成，分别是配置读取类（EasyExcelHalper）、导出Excel类（EasyExcelExportUtil）、导入Excel类（EasyExcelImportUtil）、导入Excel返回结果类（EasyExcelImportResult）和xml配置文件。

**应用环境：**
  
1. 单线程、不牵扯到并发操作。同一时间只有一人做导入操作，或只有管理员有导入权限。（受限于程序中的唯一性校验）  

2. 导入数据，列不超过26（受限于英文26个字母，不合法数据的定位），数量不大（受限于程序中数据校验，不合法数据的定位，所谓越详细就越复杂），支持常用的数据类型。


**优点：**  

1. 可以控制任意字段的类型、是否可以为空及某个字段的唯一性，避免重复导入。  

2. 精确返回某一条不合法数据的某个字段的位置，方便用户根据提示修改Excel数据。例如：B3  

3. 有严格的数据校验，避免导入的数据不能转成目标类型（数据库字段类型）。  

4. 可以冻结标题行。

**缺点：**  

1. 既要不合法数据精确定位，又要数据校验，导致控制程序逻辑较多，以数据质量换取性能。  

2. 避免重复导入功能要求不能并发操作。  

3. 数据量不能超过一个sheet页，且数据尽量少。  

4. 需要准确在xml里配置标签、字段、字段数据类型、是否可以为空、是否唯一。  


**快速入门：**  

1. 添加Maven依赖  
```
<dependency>
    <groupId>commons-configuration</groupId>
    <artifactId>commons-configuration</artifactId>
    <version>1.10</version>
</dependency>
	    
<dependency>
    <groupId>commons-collections</groupId>
    <artifactId>commons-collections</artifactId>
    <version>3.2.2</version>
</dependency>
	    
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.9</version>
</dependency>
```
2. xml配置  

默认文件名【easyExcel-config.xml】，当然如果你觉得不爽，可以在EasyExcelHalper类中修改。  
默认文件放到src/main/resources根目录下。  
无需spring加载该配置文件，EasyExcelHalper类自动去默认位置加载easyExcel-config.xml。
可以配置多个sheet页，在程序中传参选择使用哪个数据模板。  

xml的基本结构  

```xml
<excel>
    <sheets>
        <sheet>
            <title>图书</title>
  	    <freeze>true</freeze>
  	    <columns>
		<column>
	      	    <name>书名</name>
	      	    <type>java.lang.String</type>
	      	    <notNull>true</notNull>
	            <unique>true</unique>
	        </column>
	
	        <!-- column... -->
	
	    </columns>
	</sheet>
	
	<!-- sheet... -->
	
    </sheets>
</excel>

```
其中，excel、sheets、sheet、title、columns、column、name、type节点是必有的。  
3.jsp  

```jsp
<body>
    <a href="book/exportExcel">导出Excel</a>
    <form action="book/importExcel" method="post" enctype="multipart/form-data">
        <input type="file" value="导入Excel" name="bookExcel">
	<input type="submit"  value="导入Excel" > 
    </form>
</body>
```

4.Controller

```java

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
```
5. Service

```java
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
```

