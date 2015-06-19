<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="http://fonts.googleapis.com/css?family=Lobster"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/css/main.css" />
<link rel="stylesheet" href="/css/font-awesome.min.css" />
<title>카테고리 별 상품 보기</title>
</head>
<body>
	<div id="header" style="width: 100%;">
		<!-- 상단 navigator -->
		<%@ include file="./commons/_topNav.jsp"%>
		<!-- 브랜드/제품 검색바 -->
		<%@ include file="./commons/_search.jsp"%>
	</div>
	<div>
		<!-- 수평 카테고리 메뉴 -->
		<%@ include file="./commons/_horizontalCategory.jsp"%>
	</div>
	<div id="main-container" class="wrap content">
		<c:choose>
			<c:when test="${not empty brand}">
						<div style="font-size: 40px; padding:25px 0 15px 0;">${brand.brandName}</div>
				<%@ include file="./commons/_brand.jsp"%>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${not empty selectedBrand}">
						<div style="font-size: 40px; padding: 25px 0 15px 0;">${category.categoryName}<span style="font-size: 20px;"> - ${selectedBrand.brandName}</span></div>
					</c:when>
					<c:otherwise>
						<div style="font-size: 40px; padding: 25px 0 15px 0;">${category.categoryName}</div>
					</c:otherwise>
				</c:choose>
				<%@ include file="./commons/_categoryBrand.jsp"%>
			</c:otherwise>
		</c:choose>
	</div>
	<div id="item-container" class="wrap content">
		<!-- 상품 리스트 -->
		<%@ include file="./commons/_productsBox.jsp" %>
		<!-- 페이지 -->
		<%@ include file="./commons/_paging.jsp" %>
	</div>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
