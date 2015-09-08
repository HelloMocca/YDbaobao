<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/css/main.css">
<link rel="stylesheet" type="text/css" href="/css/font-awesome.min.css">
<title>YDbaobao:: 주문하기</title>
<style>
	body {
		background-color:#fff;
	}
	
	table#cart-list {
		width:100%;
		font-size:12px;
		border:1px solid #ccc;
		border-spacing:0;
	}
	table#cart-list th{
		padding:5px;
		background-color:#f8f8f8;
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
	tfoot {
		background-color:#f8f8f8;
	}
	tfoot tr{
		padding:10px;
	}

	.sold-out {
		color: red;
		font-weight: bold;
	}
	
	.quantity-container span{
		width:35px;
		display:inline-block;
	}
	
	.item-quantity {
		font-weight:bold;
	}
	
	#order-cancel-btn {
		background-color:#5D5D5D;
		float:left;
	}
</style>
</head>
<body>
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
	<div id="main-container">
		<div id="first-section" class="wrap content" style="padding:25px 0;">
			<div id="progress-info">
				<div class="on"><i class='fa fa-file-text'></i>  주문하기</div>
			</div>
			<div id="cart-section">
				<table id="cart-list">
					<thead>
						<tr>
							<th colspan="2">상품명</th>
							<th>상품가격</th>
							<th>사이즈/수량</th>
							<th>주문금액</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="item" items="${items}">
						<tr id="item_${item.itemId}" class="item-container" data-id="${item.itemId}">
							<td class="item-image-container"><img class="item-image" src="/image/products/${item.product.productImage}"></td>
							<td class="item-name-container"><span class="item-name">${item.product.productName}</span></td>
							<td><span class="product-price">${item.product.productPrice}</span></td>
							<td class="quantity-container">
								<c:forEach var="quantity" items="${item.quantities}">
									<div>
										<span>${quantity.size}</span>
										<span class="item-quantity">${quantity.value}</span>
									</div>
								</c:forEach>
							</td>
							<td><span class="order-price">${item.product.productPrice}</span></td>
						</tr>
					</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="6">
								<div style="float:left">
								</div>
								<div id="total-price" style="float:right; padding:15px; font-size:15px;">전체상품금액 :
									<span style="font-weight:800;"></span>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
				<div id="order-section">
					<button id="order-btn" class="btn">주문하기</button>
					<button id="order-cancel-btn" class="btn">취소하기</button>
				</div>
			</div>
		</div>
	</div>

	<div id="footer">footer...</div>

	<script>
	window.addEventListener('load', function() {
		var itemEl = document.querySelectorAll('.item-container');
		for (i = 0; i < itemEl.length; i++) {
			calcItemPrice(itemEl[i].dataset.id);
		}
		
		document.querySelector('#order-btn').addEventListener('click', function() {
			var checkList = document.querySelectorAll('tbody tr.item-container');
			var checkLength = checkList.length;
			var paramList = [];
			var param = 'itemList=';
			for(var i = 0; i < checkLength; i++) {
				paramList.push(checkList[i].dataset.id);
			}
			param += paramList;
			order(param);
		}, false);
		document.querySelector('#order-cancel-btn').addEventListener('click', function() {
			window.location.href = '/shop/carts';
		}, false);

		addItemsPrice();

		priceWithComma();

		totalPriceWithComma();

	}, false);

	function order(param) {
		ydbaobao.ajax({
			method : 'post',
			url : '/shop/orders',
			param : param,
			success : function(req) {
				if (req.responseText == "OK") {
					alert('주문요청이 완료되었습니다.');
					window.location.href = '/shop/orders';
				}
			}
		});
	}

	function addItemsPrice() {
		var el = document.querySelectorAll('.order-price');
		var length = el.length;
		var totalPrice = 0;
		for(var i = 0; i < length; i++) {
			totalPrice += parseInt(el[i].textContent.replace(",", ""));
		}

		document.querySelector('#total-price span').textContent = totalPrice.toLocaleString().split(".")[0];
	}
	
	function priceWithComma() {
		var el = document.querySelectorAll('.order-price');
		var length = el.length;

		for(var i = 0; i < length; i++) {
			el[i].textContent = parseInt(el[i].textContent).toLocaleString().split(".")[0];
		}
	}

	function totalPriceWithComma() {
		 	var el = document.querySelector('#total-price span');
		 	el.textContent = parseInt(el.textContent.replace(",", "")).toLocaleString().split(".")[0];
	}
	
	function calcItemPrice(itemId) {
		var itemEl = document.querySelector("#item_"+itemId);
		var productPrice = itemEl.querySelector(".product-price").textContent * 1;
		var price = 0;
		var quantities = itemEl.querySelectorAll(".item-quantity");
		for (var i = 0; i < quantities.length; i++) {
			price += quantities[i].textContent * productPrice;
		}
		itemEl.querySelector(".order-price").textContent = price;
		addItemsPrice();
	}
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
