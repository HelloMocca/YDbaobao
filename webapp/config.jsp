<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::쇼핑몰 설정</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<style>
	table#adminconfig-table tr:nth-child(odd){
		background:#f2f2f2;
	}
</style>
</head>
<body>
	<div id="header" style="width: 100%;">
		<%@ include file="./_adminTopNav.jsp"%>
	</div>
	<div id="container">
		<%@ include file="./_adminNav.jsp"%>
		<div id="content">
			<h1>쇼핑몰 설정</h1>
			<c:if test="${ not empty message}">
				<div class="message">${message}</div>
			</c:if>
			<form:form class="admin-config" action="/admin/config" method="POST"
				modelAttribute="adminConfig">
				<form:input path="adminConfigId" type="hidden"
					value="${adminConfig.adminConfigId}" />
				<table id="adminconfig-table">
					<tbody>
						<tr>
							<th>구분</th>
							<th>설정</th>
						</tr>
						<tr>
							<td><form:label path="adminDisplayProducts">페이지 당 상품 갯수</form:label></td>
							<td><form:input path="adminDisplayProducts" type="number" min="1" value="${adminConfig.adminDisplayProducts}" />개</td>
						</tr>
						<tr>
							<td><form:label path="adminPassword">관리자 비밀번호 수정</form:label></td>
							<td><form:password path="adminPassword" maxlength="20"></form:password></td>
						</tr>
						<tr>
							<td><form:label path="adminCostPerWeight">무게(Kg)당 배송비</form:label></td>
							<td><form:input path="adminCostPerWeight" type="number" value="${adminConfig.adminCostPerWeight}"></form:input>원</td>
						</tr>
					</tbody>
					<button class="btn btn-sm" type="submit">저장</button>
				</table>
			</form:form>
		</div>
	</div>
	<script src="/js/ydbaobao.js"></script>
</body>
<style>
.admin-config {
	margin: 0 auto;
	width: 400px;
}
</style>
</html>
