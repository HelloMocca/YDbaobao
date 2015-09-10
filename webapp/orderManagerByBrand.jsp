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
	.order-price, .alloc-price {
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
	
	#button-group {
		float: right;
		display:table;
		padding:25px; margin-bottom:25px;
	}
	
	#button-group button {
		padding:15px; background:#EA6576; border-radius:2px; border:0; font-size:20px;
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
	
	.quantity-container div span {
		display:inline-block;
		width:45px;
	}
	.quantity-container div .item-quantity {
		font-weight:bold;
	}
	.quantity-container div input {
		width:45px;
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
			<h1>브랜드별 주문 관리</h1>
			<div style="width: 800px;margin-right: 20px;">
				<table id="cart-list" style="text-align: center; padding-top:0px;">
					<tbody>
					<c:forEach var="brandPack" items="${brandPacks}">
						<tr><td colspan="10" class="brandHeader"><span>${brandPack.key}</span></td></tr>
						<tr>
							<th><input id="select-all-checkbox" type="checkbox" ></th>
							<th>주문자</th>
							<th colspan="2" width="130px">상품명</th>
							<th>판매가</th>
							<th>사이즈 | 주문수량 | 사입수량</th>
							<th>사입금액 | 금액</th>
							<th> </th>
						</tr>
						<c:forEach var="item" items="${brandPack.items}">
							<tr data-id="${item.itemId}">
								<td><input class="item-check" type="checkbox" onclick="calcSelectedOrder()"></td>
								<td><span class="item-customer">${item.customer.customerId}</span></td>
								<td class="item-image-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><img class="item-image" src="/image/products/${item.product.productImage}"></a></td>
								<td class="item-name-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><span class="item-name">${item.product.productName}</span></a></td>
								<td><span class="item-price">${item.product.productPrice}</span></td>
								<td class="quantity-container">
										<c:forEach var="quantity" items="${item.quantities}">
											<div>
												<span>${quantity.size}</span>
												<span class="item-quantity">${quantity.value}</span>
												<input class="alloc-quantity" type="number" min="0" max="${quantity.value}" value="0">
											</div>
										</c:forEach>
								</td>
								<td>
									<span class="alloc-price">0</span>/
									<span class="order-price">${item.price}</span>
								</td>
								<td>
									<button class="success" >사입</button>
									<button class="reject" >거부</button>
								</td>
							</tr>
						</c:forEach>
					</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="10">
								<div id="total-quantity" style="float:right; padding:15px; font-size:15px;">사입수량 :
									<span style="font-weight:800;">0</span>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="10">
								<div id="total-price" style="float:right; padding:15px; font-size:15px;">사입금액 :
									<span style="font-weight:800;">0</span>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
				<div id="button-group">
					<button id="ordersheet-button" class="btn" style="">주문서출력</button>
					<button id="submit-button" class="btn" style="padding:15px; background:#EA6576; border-radius:2px; border:0; font-size:20px;">사입처리</button>
				</div>
			</div>
		</div>
	</div>
	<script>
		window.addEventListener('load', function(){
			var i;
			var checkBtns = document.querySelectorAll('.success');
			for (i = 0; i < checkBtns.length; i++) {
				checkBtns[i].addEventListener('click', checkOrders, false);
			}
			var rejectBtns = document.querySelectorAll('.reject');
			for (i = 0; i < rejectBtns.length; i++) {
				rejectBtns[i].addEventListener('click', rejectOrder, false);
			}
			var allocInput = document.querySelectorAll('.alloc-quantity');
			for (i = 0; i < allocInput.length; i++) {
				allocInput[i].addEventListener('keyup', calcSelectedOrder, false);
			}
			document.querySelector("#submit-button").addEventListener('click', checkOrders, false);
			document.querySelector("#ordersheet-button").addEventListener('click', viewOrdersheet, false);
		}, false);

		function checkOrders(e) {
			var checkList = document.querySelectorAll('.item-check');
			var checkLength = checkList.length;
			var itemlist = [];
			var quantitylist = [];
			for(var i = 0; i < checkLength; i++) {
				if(checkList[i].checked) {
					itemlist.push(checkList[i].parentNode.parentNode.getAttribute('data-id')*1);
					quantitylist.push(checkList[i].parentNode.parentNode.querySelector('.item-quantity').value*1);
				}
			}
			if (itemlist.length === 0) {
				alert("선택된 상품이 없습니다.");
			} else {
				ydbaobao.ajax({
					method:"post",
					url:"/admin/orders/accept",
					param: "itemList="+itemlist+"&quantityList="+quantitylist,
					success: function(req) {
						if (req.responseText === "success") {
							alert("사입처리 완료.");
							for(var i = 0; i < checkLength; i++) { //처리된 아이템 제거
								if(checkList[i].checked) {
									checkList[i].parentNode.parentNode.remove();
								}
							}
						}
					}
				});
			}
		}
		
		function rejectOrder(e) {
			var itemId = e.target.parentNode.parentNode.getAttribute('data-id');
			ydbaobao.ajax({
				method: "post",
				url: "/admin/orders/reject/"+itemId,
				success: function(req){
					alert(req.responseText);
					window.location.href="/admin/orders/brands";
				}
			});
		}
		
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
		
		function calcSelectedOrder() {
			var checkList = document.querySelectorAll('.item-check');
			var checkLength = checkList.length;
			var totalPrice = 0;
			var totalQuantity = 0;
			var thisQuantity = 0;
			var allocQuantities = [];
			var itemPrice = 0;
			for (var i = 0; i < checkLength; i++) {
				if (checkList[i].checked) {
					itemPrice = checkList[i].parentNode.parentNode.querySelector(".item-price").textContent*1;
					allocQuantities = checkList[i].parentNode.parentNode.querySelectorAll(".alloc-quantity");
					for (var r = 0; r < allocQuantities.length; r++) {
						totalPrice += itemPrice * allocQuantities[r].value*1;
						totalQuantity += allocQuantities[r].value*1;
					}
				}
			}
			document.querySelector('#total-price span').textContent = totalPrice.toLocaleString().split(".")[0];
			document.querySelector('#total-quantity span').textContent = totalQuantity;
		}
		
		function viewOrdersheet() {
			var checkList = document.querySelectorAll('.item-check');
			var checkLength = checkList.length;
			var itemIdlist = [];
			for(var i = 0; i < checkLength; i++) {
				if(checkList[i].checked) {
					itemIdlist.push(checkList[i].parentNode.parentNode.getAttribute('data-id')*1);
				}
			}
			if (itemIdlist.length === 0) {
				alert("선택된 상품이 없습니다.");
			} else {
				window.open("/admin/orders/ordersheet/"+itemIdlist);
			}
		}
		
		function calcAllocPrice(e) {
			e.target.parentNode.parentNode.parentNode.querySelector(".item-price");
		}
		
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
