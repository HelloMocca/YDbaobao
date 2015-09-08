<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="/css/main.css" />
<link rel="stylesheet" type="text/css" href="/css/font-awesome.min.css" />
<title>Yuandamaoyi:: Welcome</title>
</head>
<body style="background:none; overflow:hidden;">
	<c:if test="${not empty sessionCustomer}">
		<c:redirect url="indexmain" />
	</c:if>
	<div id="header" style="background-color:rgba(255,255,255,.8);">
		<!-- 상단 navigator -->
		<%@ include file="./commons/_topNav.jsp"%>
	</div>
	<div id="main-container">
		<div id="first-section" class="wrap" style="height: 635px;">
			<div class="loginBox">
				<form:form modelAttribute="customer" method="post" action="/shop/login">
					<h1>LOGIN</h1>
					<label for="customerId">ID</label>
					<form:input path="customerId" />
					<br/>
					<label for="customerPassword">PASSWORD</label>
					<form:password path="customerPassword" />
					<br/>
					<c:if test="${not empty message}">
						<span class="error" style="color:#F15F5F; font-size:12px; padding:5px 10px; position:relative; display:block;">${message}</span>
					</c:if>
					<button type="submit">ENTER</button>
				</form:form>
			</div>
		</div>
	</div>
	<!-- 
	<div id="footer">
		<%@ include file="./commons/_footer.jsp"%>
	</div>
	 -->
	<div id="bg" style="position:fixed; top:0; z-index:-1;width:100%; height:100%; background-image:url('/image/bgsample.jpg'); background-repeat:no-repeat; background-size:100%">
	</div>
</body>
</html>