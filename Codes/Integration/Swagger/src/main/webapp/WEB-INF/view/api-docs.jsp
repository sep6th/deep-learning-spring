<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>API DOCS</title>
<link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700|Source+Code+Pro:300,600|Titillium+Web:400,600,700" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="static/plug/swagger-ui/swagger-ui.css" >
<link rel="icon" type="image/png" href="static/plug/swagger-ui/favicon-32x32.png" />
<link rel="icon" type="image/png" href="static/plug/swagger-ui/favicon-16x16.png" />
<style>
html {
  box-sizing: border-box;
  overflow: -moz-scrollbars-vertical;
  overflow-y: scroll;
}
*, *:before, *:after {
  box-sizing: inherit;
}
body {
  margin:0;
  background: #fafafa;
}
</style>
</head>
<body>
	<div id="swagger-ui"></div>
</body>
<script src="static/plug/swagger-ui/swagger-ui-bundle.js"> </script>
<script src="static/plug/swagger-ui/swagger-ui-standalone-preset.js"> </script>
<script>
window.onload = function(){
  const ui = SwaggerUIBundle({
    url: "http://127.0.0.1:8090/v2/api-docs",
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  })
  window.ui = ui;
}
</script>
</html>