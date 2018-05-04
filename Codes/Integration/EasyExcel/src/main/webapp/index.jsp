<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>Welcome</title>
	</head> 
	<body>
		<a href="book/exportExcel">导出Excel</a>
		<form action="book/importExcel" method="post" enctype="multipart/form-data">
			<input type="file" value="导入Excel" name="bookExcel">
			<input type="submit"  value="导入Excel" > 
		</form>
	</body>
</html>
