<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::주문관리</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<style>
td {
	width:150px;
}
img.hover{
	display:block;
	float:left;
	max-width:150px;
	display:none;
	position: absolute;
}

img.snap{
	display:block;
	float:left;
	max-width:150px;
}
</style>
</head>
<body>
	<c:if test="${null ne errorMessage}">
		<script>
			alert("${errorMessage}");
		</script>
	</c:if>
	<div id="header" style="width: 100%;">
		<%@ include file="./_adminTopNav.jsp"%>
	</div>
	<div id="container">
		<%@ include file="./_adminNav.jsp" %>
		<div id="content">
			<h1>첫화면 관리</h1>

 			<form id="imgForm" enctype="multipart/form-data" action="/admin/indexImages/" method="post">
				<input type="file" id="imageFile"  name="imageFile" accept="image/x-png, image/gif, image/jpeg" style="display:none;" onchange="changeImage(this)"/>
			</form>
			<table>
				<tr>
					<c:forEach  var="path" items="${indexImages}" varStatus="status" begin="0" end="3" step="1" >
						<td>
							<a href="#">
								<c:if test="${path.indexImageName == null}">
									<i onclick="addClick()" style="color: rgb(234, 101, 118); font-size: 100px; width: 100%; text-align: center;" class="fa fa-plus-circle"></i>
								</c:if>
								<c:if test="${path.indexImageName != null}">
									<img id="hoverImage${status.index}" class="hover" onclick="deleteClick(this)" onmouseout="mouseOut(${status.index})" src="/image/delete_hover.png" data-id="${path.indexImageId}">
									<img class="snap" src="/image/index/${path.indexImageName}" onmouseover="mouseOver(${status.index})">
								</c:if>
							</a>
						</td>
					</c:forEach>
				</tr>
				<tr>
					<c:forEach  var="path" items="${indexImages}" varStatus="status" begin="4" end="7" step="1" >
						<td>
							<a href="#">
								<c:if test="${path.indexImageName == null}">
									<i onclick="addClick()" style="color: rgb(234, 101, 118); font-size: 100px; width: 100%; text-align: center;" class="fa fa-plus-circle"></i>
								</c:if>
								<c:if test="${path.indexImageName != null}">
									<img id="hoverImage${status.index}" class="hover" onclick="deleteClick(this)" onmouseout="mouseOut(${status.index})" src="/image/delete_hover.png" data-id="${path.indexImageId}">
									<img class="snap" src="/image/index/${path.indexImageName}" onmouseover="mouseOver(${status.index})">
								</c:if>
							</a>
						</td>
					</c:forEach>
				</tr>
			</table>
		</div>
	</div>
	<script>
	function addClick() {
		document.querySelector('#imageFile').click();
	}

	function changeImage(input) {
		var form = document.querySelector("#imgForm");
		form.action = action="/admin/indexImages/";
		form.submit();
	}

	function mouseOver(imgIndex) {
		document.querySelector("#hoverImage"+imgIndex).style.display = "block";
	}

	function mouseOut(imgIndex) {
		document.querySelector("#hoverImage"+imgIndex).style.display = "none";
	}

	function deleteClick(el) {
		var indexImageId = el.dataset.id;
		var form = document.querySelector("#imgForm");
		form.action = action="/admin/indexImages/"+indexImageId;
		form.submit();
	}
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>