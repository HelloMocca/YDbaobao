<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::주문관리</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<style>
	ul#orderedBrandList {
		list-style: none;
		width: 800px;
		margin-right: 20px;
	}
	#orderedBrandList li {
		font-size:12px;
		display:inline-block;
		margin:2px 0;
		background-color: #FFD8D8;
	}
	#orderedBrandList li a {
		display:block;
		padding:10px;
		width:120px;
		text-decoration: none;
		color: #454545;
	}
	#orderedBrandList li a span {
		display:block;
		margin-left:5px;
	}
	#orderedBrandList li a:hover {
		text-decoration: underline;
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
			<h1>주문된 브랜드 목록</h1>
			<ul id="orderedBrandList">
				<c:forEach var="brand" items="${brandList}">
					<li class='list-item'>
						<a href="/admin/orders/brand/${brand.brandId}"><span>${brand.brandName}</span></a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</body>
</html>
