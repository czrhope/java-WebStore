<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<!-- 登录 注册 购物车... -->
<script>
	function logOut(){
		if(confirm("您确认要退出吗？")){
			location.href = "${pageContext.request.contextPath }/user?method=logOut";
		}
	}
</script>
<div class="container-fluid">
	<div class="col-md-4">
		<a href="${pageContext.request.contextPath }"><img src="img/logo2.png" /></a>
	</div>
	<div class="col-md-5">
		<img src="img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
			<c:if test="${empty user }">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<li><a href="login.jsp">登录</a></li>&nbsp;&nbsp;
				<li><a href="register.jsp">注册</a></li>&nbsp;&nbsp;
				<li><a href="cart.jsp">购物车</a></li>
			</c:if>
			<c:if test="${!empty user }">
				<li>
					<a href="javascript:" style="color:red">${user.username }&nbsp;&nbsp;</a></option>
					<a href="javascript:;" onclick="logOut()">退出登录&nbsp;&nbsp;</a>
				</li>
				<li><a href="cart.jsp">购物车</a></li>
				<li><a href="${pageContext.request.contextPath }/transaction?method=findOrder">我的订单</a></li>
			</c:if>
		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${pageContext.request.contextPath }">首页</a>
			</div>
			
			<!-- 通过ajax获取类别信息 -->
			<script type="text/javascript">
				$(function(){
					var content = "";
					$.post(
						"${pageContext.request.contextPath}/product?method=getCategoryList",
						function(data){
							for(var i=0;i<data.length;i++){
								//从服务器端获得数据填充到页面中
								content += "<li><a href='${pageContext.request.contextPath}/product?method=getProductList&cid="+data[i].cid
										+"'>"+data[i].cname+"</a></li>";
							}
							$("#categoryList").html(content);
						},
						"json"
					);
				});
			</script>
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav" id="categoryList">
					
				</ul>
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>
	</nav>
</div>