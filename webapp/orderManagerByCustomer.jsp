<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>관리자페이지::주문관리</title>
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
		/* background-color:#f8f8f8; */
	}
	tbody td{
		padding:10px 0;
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
		font-weight:500;
		border-top:2px solid #ccc;
	}
</style>
</head>
<body>
	<div id="header" style="width: 100%;">
		<%@ include file="./_adminTopNav.jsp"%>
	</div>
	<div id="container">
		<%@ include file="./_adminNav.jsp"%>
		<div id="content" >
			<h1>회원별 주문 관리</h1>
			<div style="width: 800px;margin-right: 20px;">
				<table id="cart-list" style="text-align: center; padding-top:0px;">
					<tbody>
					<c:forEach var="customerPack" items="${customerPacks}">
						<tr><td colspan="9" class="brandHeader"><span>${customerPack.key}</span></td></tr>
						<tr>
							<th>브랜드</th>
							<th colspan="2">상품설명</th>
							<th style="width:35px">사이즈</th>
							<th>판매가</th>
							<th style="width:45px;">주문수량</th>
							<th>승인수량</th>
							<th>주문금액</th>
							<th> </th>
						</tr>
						<c:forEach var="item" items="${customerPack.items}">
							<tr data-id="${item.itemId}">
								<td><span class="item-customer">${item.product.brand.brandName}</span></td>
								<td class="item-image-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><img class="item-image" src="/image/products/${item.product.productImage}"></a></td>
								<td class="item-name-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><span class="item-name">${item.product.productName}</span></a></td>
								<td><span class="item-size">${item.size}</span></td>
								<td><span class="item-price">${item.product.productPrice}</span></td>
								<td><span>${item.quantity}</span></td>
								<td><input style="width:40px;" type="number" class ="item-quantity" name="quantity" value ="${item.quantity}"/>
								<td><span class="order-price">${item.price}</span></td>
								<td><input type="button" class="success" value="승인">
								<input type="button" class="reject" value="삭제"></td>
							</tr>
						</c:forEach>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script>
		window.addEventListener('load', function(){
			var i;
			var checkBtns = document.querySelectorAll('.success');
			for (i = 0; i < checkBtns.length; i++) {
				checkBtns[i].addEventListener('click', checkOrder, false);
			}
			var rejectBtns = document.querySelectorAll('.reject');
			for (i = 0; i < rejectBtns.length; i++) {
				rejectBtns[i].addEventListener('click', rejectOrder, false);
			}
		}, false);

		function checkOrder(e) {
			var itemId = e.target.parentNode.parentNode.getAttribute('data-id');
			var quantity = e.target.parentElement.parentElement.querySelector(".item-quantity").value;
			ydbaobao.ajax({
				method: "post",
				param: "quantity="+quantity,
				url: "/admin/orders/accept/"+itemId,
				success: function(req){
					alert(req.responseText);
					window.location.href="/admin/orders/customers";
				}
			});
		}
		
		function rejectOrder(e) {
			var itemId = e.target.parentNode.parentNode.getAttribute('data-id');
			ydbaobao.ajax({
				method: "post",
				url: "/admin/orders/reject/"+itemId,
				success: function(req){
					alert(req.responseText);
					window.location.href="/admin/orders/customers";
				}
			});
		}
		
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
