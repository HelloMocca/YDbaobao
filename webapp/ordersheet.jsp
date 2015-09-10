<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::주문서</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<style>
	table#cart-list {
		width:100%;
		font-size:12px;
		border:1px solid #ccc;
		border-spacing:0;
	}
	table#cart-list th{
		padding:5px;
		padding-top:10px;
		background-color:#c8c8c8;
		color:#454545;
		font-weight:bold;
	}
	tbody td{
		padding:10px 0;
		border-top:1px solid #ddd;
		border-left:1px solid #ddd;
	}
	table#cart-list td {
		font-size:15px;
	}
	
	table tr:nth-child(even){
		background:#f1f1f1;
	}
	table tr:nth-child(odd){
		background:#ffffff;
	}
	
	.item-name-container {
		text-align:left;
	}
	.item-image {
		width:50px;
		height:50px;
	}
	.order-price {
		font-weight:800;
	}

	.waiting {
		color: #DBC000;
	}
	
	.success {
		color: #62C15B;
	}
	
	.reject, .cancel {
		color: #F15F5F;
	}
	
	button.info, button.success, button.reject {
		border: 0;
		padding: 5px;
		color: #fff;
	}
	
	button.info {
		background-color: #4374D9;
		border-bottom:2px solid #002C91;
	}
	
	button.success {
		background-color: #62C15B;
		border-bottom:2px solid #086701;
	}
	
	button.reject {
		background-color: #F15F5F;
		border-bottom:2px solid #840000;
	}
	
	.brandHeader {
		background-color:#c8c8c8;
		font-size:20px;
		font-weight:800;
		border-top:2px solid #ccc;
	}
	
	#info-table {
		width:800px;
		border-spacing:0;
		font-size:12px;
		border-bottom:1px solid #f8f8f8;
	}
	#info-table td:nth-child(odd) {
		background:#c8c8c8;
		width:80px;
		font-weight:bold;
	}
	#info-table td {
		padding-left:15px;
	}
</style>
</head>
<body>
	<div style="border:0; padding:50px 25px; display:table; margin:0 auto;">
		<div id="" >
			<h1>주문서</h1>
			<table id="info-table">
				<tr>
					<td padding-left:15px;">브랜드</td><td>${itemList[0].product.brand.brandName}</td>
					<td>일자</td><td id="today-container"></td>
					<td>주문자</td><td>YDBAOBAO</td>
					<td>연락처</td><td>010-0000-0000</td>
				</tr>
			</table>
				<table id="cart-list" style="text-align: center; padding-top:0px; width:800px;">
					<tbody>
						<tr>
							<th width="75px">상품번호</th>
							<th colspan="2" style="width:200px">상품명</th>
							<th >사이즈</th>
							<th >수량</th>
						</tr>
						<c:forEach var="item" items="${itemList}">
							<tr data-id="${item.itemId}">
								<td><span>${item.itemId}</span></td>
								<td width="50px" class="item-image-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><img class="" style="width:50px;" src="/image/products/${item.product.productImage}"></a></td>
								<td width="50px" class="item-name-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><span class="item-name" style="margin-left:25px;">${item.product.productName}</span></a></td>
								<td>
								<c:forEach var="quantity" items="${item.quantities}">
									<div><span class="item-size">${quantity.size}</span></div>
									<div><span class="ordered-quantity">${quantity.value}</span></div>
								</c:forEach>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</div>
	</div>
	<script>
		window.addEventListener('load', function(){
			document.querySelector("#today-container").textContent = new Date().toLocaleDateString();
		}, false);
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
