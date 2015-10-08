<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::완료 주문 관리</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<style>
table {
	font-size:13px;
}
table td {
	text-align:center;
}
table tbody tr:nth-child(even) {
	background-color:#f1f1f1;
}

table tfoot {
	background-color:#dcdcdc;
}
table tfoot td{
	padding:5px 0;
}
label {
	display:inline-block;
	width:85px;
	font-size:13px;
	background:#454545;
	color:white;
	padding:5px;
}
form {
	padding:5px;
	outline:1px solid #ccc;
}
</style>
</head>
<body>
	<div id="header" style="width: 100%;">
		<%@ include file="./_adminTopNav.jsp"%>
	</div>
	<div id="container" style="width:1200px">
		<%@ include file="./_adminNav.jsp"%>
		<div id="content">
			<c:choose>
				<c:when test="${not empty date}"><h1>${date}</h1></c:when>
				<c:otherwise><h1>${customerId}</h1></c:otherwise>
			</c:choose>
			<div>
				<form action="/admin/orders/shipped/" method="get">
					<label for="searchByDate">날짜별 조회</label>
					<input id="searchByDate" name="date" type="text" />
					<input type="submit" value="조회">
				</form>
			</div>
			<div>
				<form action="/admin/orders/shipped/customerid" method="get">
					<label for="searchByCustomerId">주문자별 조회</label>
					<input id="searchByCustomerId" name="customerId" type="text" />
					<input type="submit" value="조회">
				</form>
			</div>
			<table style="width:100%;">
				<thead>
					<tr>
						<th>#</th>
						<c:choose>
							<c:when test="${not empty date}"><th>주문자</th></c:when>
							<c:otherwise><th>배송일</th></c:otherwise>
						</c:choose>
						<th>배송비</th>
						<th>추가할인</th>
						<th>청구금액</th>
						<th>납입금액</th>
						<th>남은금액</th>
						<th>&nbsp</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="order" items="${orders}">
					<tr class="order-container" data-id="${order.orderId}">
						<td>${order.orderId}</td>
						<c:choose>
							<c:when test="${not empty date}"><td>${order.customerId}</td></c:when>
							<c:otherwise><td>${order.orderDate}</td></c:otherwise>
						</c:choose>
						<td class="shippingCost">${order.shippingCost}</td>
						<td class="extraDiscount">${order.extraDiscount}</td>
						<td class="orderPrice">${order.orderPrice}</td>
						<td class="paiedPrice">${order.paiedPrice}</td>
						<td class="remainPrice">${order.orderPrice - order.shippingCost}</td>
						<td>
							<button><i class="fa fa-archive"></i> 상세내역</button>
						</td>
					</tr>
					</c:forEach>
				</tbody>
				<tfoot>	
					<tr>
						<td id="order-count">0</td>
						<td></td>
						<td id="ship-count">0</td>
						<td id="dc-count">0</td>
						<td id="op-count">0</td>
						<td id="pp-count">0</td>
						<td id="rp-count">0</td>
						<td></td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
	<script>
		window.addEventListener('load', function(){
			var orderCount = 0; var shipCount = 0; var dcCount = 0; var opCount = 0; var ppCount = 0; var rpCount = 0;
			orderCount = document.querySelectorAll(".order-container").length;
			var shipCosts = document.querySelectorAll(".shippingCost");
			for (var i = 0; i < shipCosts.length; i++) {
				shipCount += shipCosts[i].textContent * 1;
			}
			var discounts = document.querySelectorAll(".extraDiscount");
			for (var i = 0; i < discounts.length; i++) {
				dcCount += discounts[i].textContent * 1;
			}
			var orderPrices = document.querySelectorAll(".orderPrice");
			for (var i = 0; i < orderPrices.length; i++) {
				opCount += orderPrices[i].textContent * 1;
			}
			var paiedPrices = document.querySelectorAll(".paiedPrice");
			for (var i = 0; i < paiedPrices.length; i++) {
				ppCount += paiedPrices[i].textContent * 1;
			}
			var remainPrices = document.querySelectorAll(".remainPrice");
			for (var i = 0; i < remainPrices.length; i++) {
				rpCount += remainPrices[i].textContent * 1;
			}
			document.querySelector("#order-count").innerHTML = orderCount + "건";
			document.querySelector("#ship-count").innerHTML = parseInt(shipCount).toLocaleString().split(".")[0];
			document.querySelector("#dc-count").innerHTML = parseInt(dcCount).toLocaleString().split(".")[0];
			document.querySelector("#op-count").innerHTML = parseInt(opCount).toLocaleString().split(".")[0];
			document.querySelector("#pp-count").innerHTML = parseInt(ppCount).toLocaleString().split(".")[0];
			document.querySelector("#rp-count").innerHTML = parseInt(rpCount).toLocaleString().split(".")[0];
		}, false);
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>