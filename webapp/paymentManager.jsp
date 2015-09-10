<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::회원별 출납관리</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<style>
	table {
		font-size:13px;
	}
	#pay-table input{
		width:96%;
	}
	
	table th {
		background-color:#c8c8c8;
		color:#454545;
	}
	
	table td {
		text-align:center;
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
			<h1>${customerId}</h1>
			<table id="summary-table" style="width:800px; text-align:center;">
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
						<th style="width:70px;"></th>
					</tr>
					<tr>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td><input id="paydate" type="text" placeholder="YYYY-MM-DD"></td>
						<td><input id="payamount" type="text" ></td>
						<td><button class="btn" onclick="createNewPay()">추가</button></td>
					</tr>
				</tfoot>
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
	<script>
		var payments;
		var totalPurchase = 0;
		var totalRecall = 0;
		var totalPay = 0;
		var totalRemain = 0;
		window.addEventListener('load', function(){
			ydbaobao.ajax({
				method:"get",
				url:"/admin/payment/read/${customerId}",
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
								lastTd = "<td><a class='btn' href='/admin/payment/receipt/"+payment.paymentId+"'><i class='fa fa-navicon'></i>  주문서보기</a></td>";
								break;
					case "I": table = document.querySelector('#pay-table tbody');
								totalPay += payment.amount;
								lastTd = "<td><button class='btn'>수정</button>  <button class='btn'>삭제</button></td>"
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
		
		function createNewPay() {
			var date = document.querySelector("#paydate").value;
			var amount = document.querySelector("#payamount").value;
			amount = amount.replace(/,/g, "");
			
			ydbaobao.ajax({
				method:"post",
				url:"/admin/payment/create",
				param:"customerId=${customerId}&amount="+amount+"&paymentDate="+date,
				success:function(req) {
					if(req.responseText === 'success') {
						alert('납입내역이 추가되었습니다.');
						document.querySelector("#paydate").value = "";
						document.querySelector("#payamount").value = "";
						totalPay = (totalPay*1) + amount*1;
						totalRemain -= amount;
						document.querySelector('#pay-summary').innerHTML = parseInt(totalPay).toLocaleString().split(".")[0];
						document.querySelector('#remain-summary').innerHTML = parseInt(totalRemain).toLocaleString().split(".")[0];
						document.querySelector('#pay-table tbody').appendChild(ydbaobao.createElement({
							name:"tr",
							content:"<td><input type='text' value='"+date+"'></td><td><input type='text' value='"+parseInt(amount).toLocaleString().split(".")[0]+"'></td><td><button>수정</button>  <button>삭제</button></td>"
						}));
						
					} else {
						alert('납입내역 등록에 실패하였습니다.');
					}
				}
			});
		}
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>