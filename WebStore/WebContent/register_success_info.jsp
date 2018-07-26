<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>注册成功</title>
<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
</head>
<script type="text/javascript">
	var time=5;
	$(function(){
		start = setInterval(count,1000);
	});
	function count(){
		time--;
		if(time==0){
			window.clearInterval(start);
			location.href="${pageContext.request.contextPath}/login.jsp";
		}
		$("#count").html(time);
	}
</script>
<body>
<h1>恭喜您注册成功，<span id="count" style="color:red">5</span>s后将进入登录页面</h1>
<h2 align="center">啦啦啦啦啦啦啦</h2>
</body>
</html>