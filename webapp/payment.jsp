<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="/css/main.css">
<link rel="stylesheet" type="text/css" href="/css/font-awesome.min.css">
<style>
html, body {
	background-color: #fff;
}

#payment-section {
	margin:25px 0px;
}

.order-head {
	background-color: lightgray;
}

table{
	width: 100%;
	font-size: 12px;
	border: 1px solid #ccc;
	border-spacing: 0;
}

table th {
	padding: 5px;
	background-color: #f8f8f8;
}

tbody td {
	padding: 10px 0;
	text-align: center;
}

.item-name-container {
	text-align: left;
}

.item-image {
	width: 50px;
	height: 50px;
}

tfoot {
	background-color: #f8f8f8;
}

tfoot tr {
	padding: 10px;
}

tr.border_top td {
  border-top:1pt solid #ccc;
}

button {
	border: 0;
	padding: 5px;
}

</style>
<title>YDbaobao:: 결제조회</title>
</head>
<body>
    <div id="main-container">
		<div id="header">
			<!-- 상단 navigator -->
			<%@ include file="./commons/_topNav.jsp"%>
			<!-- 브랜드/제품 검색바 -->
			<%@ include file="./commons/_search.jsp"%>
		</div>
		<div>
			<!-- 수평 카테고리 메뉴 -->
			<%@ include file="./commons/_horizontalCategory.jsp"%>
		</div>
		<div id="first-section" class="wrap content">
			<div id="progress-info">
				<div class="on"><i class='fa fa-calculator'></i>  결제 관리</div>
			</div>
			<div id="payment-section">
				<table id="summary-table" style="width:800px; text-align:center; margin:0 auto;">
					<tr>
						<th>총 구매금액</th>
						<th>총 납입금액</th>
						<th>총 반품금액</th>
						<th>잔액</th>
					</tr>
					<tr>
						<td id="purchase-summary"></td>
						<td id="pay-summary"></td>
						<td id="recall-summary"></td>
						<td id="remain-summary"></td>
					</tr>
				</table>
				<div style="display:table; margin:0 auto;">
					<table id="purchase-table" style="float:left; width: 250px; margin-top:25px;">
						<tbody>
							<tr>
								<th colspan='3'>구매내역</th>
							</tr>
							<tr>
								<th>발생일</th>
								<th>금액</th>
								<th>주문서</th>
							</tr>
							<tr>
							</tr>
						</tbody>
					</table>
				
					<table id="pay-table" style="float:left; margin-left:10px; width: 270px; margin-top:25px;">
						<tbody>
							<tr>
								<th colspan='3'>납입내역</th>
							</tr>
							<tr>
								<th>발생일</th>
								<th>금액</th>
							</tr>
							<tr>
							</tr>
						</tbody>
					</table>
				
					<table id="recall-table" style="float:left; margin-left:10px; width: 250px; margin-top:25px;">
						<tbody>
							<tr>
								<th colspan='3'>반품내역</th>
							</tr>
							<tr>
								<th>발생일</th>
								<th>금액</th>
								<th>품목</th>
							</tr>
							<tr>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="footer">
		<%@ include file="./commons/_footer.jsp"%>
	</div>
	<script>
		var payments;
		var totalPurchase = 0;
		var totalRecall = 0;
		var totalPay = 0;
		var totalRemain = 0;
		window.addEventListener('load', function(){
			ydbaobao.ajax({
				method:"get",
				url:"/shop/payments/read",
				success:function(req){
					payments = JSON.parse(req.responseText);
					refreshPayments();
				}
			});
		}, false);
		
		function refreshPayments(){
			var i;
			var paymentsLength = payments.length;
			var payment;
			var table;
			var lastTd;
			for (i = 0; i < paymentsLength; i++) {
				payment = payments[i];
				switch (payment.paymentType) {
					case "P": table = document.querySelector('#purchase-table tbody');
								totalPurchase += payment.amount;
								lastTd = "<td><button><i class='fa fa-navicon'></i>  주문서보기</button></td>";
								break;
					case "C": table = document.querySelector('#recall-table tbody');
								totalRecall += payment.amount;
								break;
				}
				if (payment.paymentType !== "I") {
					table.appendChild(ydbaobao.createElement({
						name:"tr",
						content:"<td>"+payment.paymentDate.split(" ")[0]+"</td><td>"+ parseInt(payment.amount).toLocaleString().split(".")[0]+"</td>"+lastTd
					}));
				} else {
					table.appendChild(ydbaobao.createElement({
						name:"tr",
						content:"<td><input type='text' value='"+payment.paymentDate.split(" ")[0]+"'></td><td><input type='text' value='"+ parseInt(payment.amount).toLocaleString().split(".")[0]+"'></td>"+lastTd
					}));
				}
			}
			totalRemain = totalPurchase - (totalPay + totalRecall);
			document.querySelector('#purchase-summary').innerHTML =  parseInt(totalPurchase).toLocaleString().split(".")[0];
			document.querySelector('#pay-summary').innerHTML = parseInt(totalPay).toLocaleString().split(".")[0];
			document.querySelector('#recall-summary').innerHTML = parseInt(totalRecall).toLocaleString().split(".")[0];
			document.querySelector('#remain-summary').innerHTML = parseInt(totalRemain).toLocaleString().split(".")[0];
		}
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
