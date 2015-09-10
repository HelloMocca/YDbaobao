<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="/css/main.css" />
<link rel="stylesheet" type="text/css" href="/css/font-awesome.min.css" />
<title>YDbaobao:: 관리자페이지</title>
</head>
<body>
	<div id="header" style="width: 100%;">
		<!-- 상단 navigator -->
		<%@ include file="../commons/_topNav.jsp"%>
	</div>
	<div class="joinForm" style="height: 100%;">
		<form id="admin" method="post" action="/admin/check">
			<h1>관리자 로그인</h1>
			<input type="password" name="adminPassword" placeholder="비밀번호 입력" /><br />
			<button type="submit">확인</button>
		</form>
	</div>
	<div id="footer">
		<%@ include file="../commons/_footer.jsp"%>
	</div>
</body>
</html>