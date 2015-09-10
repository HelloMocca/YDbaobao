<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>영수증</title>
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
		font-weight:800;
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
			<h1>구매 영수증</h1>
			<div style="width: 800px;margin-right: 20px;">
				<table id="cart-list" style="text-align: center; padding-top:0px;">
					<tbody>
						<tr><td colspan="10" class="brandHeader"><span>${item.customer.customerId}</span></td></tr>
						<tr>
							<th><input id="select-all-checkbox" type="checkbox" ></th>
							<th>주문자</th>
							<th colspan="2">상품명</th>
							<th style="width:35px">판매가</th>
							<th>사이즈</th>
							<th>수량</th>
							<th>금액</th>
							<th> </th>
						</tr>
						<c:forEach var="item" items="${items}">
							<tr data-id="${item.itemId}">
								<td><input class="item-check" type="checkbox"></td>
								<td><span class="item-customer">${item.customer.customerId}</span></td>
								<td class="item-image-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><img class="item-image" src="/image/products/${item.product.productImage}"></a></td>
								<td class="item-name-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><span class="item-name">${item.product.productName}</span></a></td>
								<td><span class="item-price">${item.product.productPrice}</span></td>
								<td><span class="item-size">${item.size}</span></td>
								<td><input style="width:40px;" type="number" class ="item-quantity" name="quantity" value ="${item.quantity}" onchange="checkValidQuantity(this)"/>
								<td><span class="order-price">${item.price}</span></td>
								<td></td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="10">
								<div id="total-price" style="float:right; padding:15px; font-size:15px;">합계 :
									<span style="font-weight:800;">0</span>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
				<div id="submit-buttons" style="padding:25px; margin-bottom:25px;">
				</div>
			</div>
		</div>
	</div>
	<script>
		window.addEventListener('load', function(){
			var totalPrice = 0;
			var prices = document.querySelectorAll('.order-price');
			for (var i = 0; i < prices.length; i++) {
				totalPrice += prices[i].textContent * 1;
			}
			document.querySelector('#total-price span').textContent = totalPrice;
		}, false);
		
		document.querySelector('#select-all-checkbox').addEventListener('click', function(e) {
			var checkedItems = document.querySelectorAll('.item-check');
			var length = checkedItems.length;

			//전체선택 해제
			if(e.target.classList.contains('checked')) {
				e.target.classList.remove('checked');
				for(var i = 0; i < length; i++) {
					checkedItems[i].checked = false;
				}
				calcSelectedOrder();
				return;
			}

			e.target.classList.add('checked');
			for(var j = 0; j < length; j++) {
				checkedItems[j].checked = true;
			}
			calcSelectedOrder();
		});
		
		function checkValidQuantity(e) {
			var quantity = e.value * 1;
			var orderedQuantity = e.parentNode.parentNode.querySelector('.ordered-quantity').textContent * 1;
			if (orderedQuantity < quantity) {
				e.value = orderedQuantity;
			}
			calcSelectedOrder();
		}
		
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
