<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::설정</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
</head>
<body>
	<div id="header" style="width: 100%;">
		<%@ include file="./_adminTopNav.jsp"%>
	</div>
	<div id="container">
		<%@ include file="./_adminNav.jsp"%>
		<div id="content">
			<h1>관리자 설정</h1>
			<c:if test="${ not empty message}">
				<div class="message">${message}</div>
			</c:if>
			<form:form class="admin-config" action="/admin/config" method="POST"
				modelAttribute="adminConfig">
				<form:input path="adminConfigId" type="hidden"
					value="${adminConfig.adminConfigId}" />
				<table>
					<tbody>
						<tr>
							<th>구분</th>
							<th>설정</th>
						</tr>
						<tr>
							<td><form:label path="adminDisplayProducts">페이지 당 상품 갯수</form:label></td>
							<td><form:input path="adminDisplayProducts" type="number" min="1" value="${adminConfig.adminDisplayProducts}" /></td>
						</tr>
						<tr>
							<td><form:label path="adminPassword">관리자 비밀번호 수정</form:label></td>
							<td><form:password path="adminPassword" maxlength="20"></form:password></td>
						</tr>
					</tbody>
					<input type="submit" value="저장" />
				</table>
			</form:form>
		</div>
	</div>
	<script>
	
	/* 주석처리 한 사람 : jyb
	 * id="config" 를 가진 element가 존재하지 않음
	 * 회원 등급 변경 기능이 없음
	 */
	// window.addEventListener('load', function() {
		
	// 	document.body.querySelector('#config').addEventListener('', function(e) {
			
	// 		ydbaobao.ajax({
	// 			method:'put',
	// 			url:'/admin/config/',
	// 			success: function(req){
	// 				if(req.responseText === 'success') {
	// 					alert('회원 등급이 변경되었습니다');
	// 				}
	// 			}
	// 		});			
	// 	}, false);
	// }, false);
	
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
<style>
.admin-config {
	margin: 0 auto;
	width: 400px;
}
</style>
</html>
