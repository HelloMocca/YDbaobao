<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::배송대기주문관리</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<style>
	#list-header {
		width:100%;
		background-color:#454545;
		color:white;
	}
	
	#list-footer {
		margin-top: 5px;
		width: 100%;
		background-color: #f1f1f1;
		border: 1px solid #ccc;
		font-weight:700;
	}
	
	#list-header div, .list-header span, #list-footer div {
		width:23%;
		height:30px;
		line-height:30px;
		text-align:center;
		display:inline-block;
	}

	.table-container {
		padding:10px;
		margin-top:3px;
		background:#f2f2f2;
		border:1px solid #ccc;
	}
	
	.table-container:nth-child(odd) {
		background: #f2f2f2;
	}
	
	table {
		width:100%;
		font-size:12px;
		border:1px solid #ccc;
		border-spacing:0;
		margin:15px 0;
		background:#fff
	}
	table th{
		padding:5px;
	}
	tbody td{
		padding:10px 0;
	}
	table tfoot {
		background:#f8f8f8;
	}
	table tfoot tr td{
		padding:10px 0;
	}
	.item-name-container {
		text-align:left;
	}
	.item-image {
		width:50px;
		height:50px;
	}
	.item-price {
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
	
	.quantity-container span{
		width:35px;
		display:inline-block;
	}
	.quantity-container .quantity {
		font-weight:bold;
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
			<h1>배송대기 주문 관리</h1>
			<div id="list-header">
				<div>주문자</div>
				<div>수량</div>
				<div>금액</div>
				<div> &nbsp </div>
			</div>
			<c:forEach var="customerPack" items="${customerPacks}">
			<div class="table-container">
					<div class="list-header">
						<span>${customerPack.key}</span>
						<span class="total-order-quantities">${customerPack.quantity}</span>
						<span class="total-order-prices">${customerPack.price}</span>
						<span><button class="tableOpenBtn">상세내역</button></span>
					</div>
					<table id="table_${customerPack.key}" style="text-align: center; padding-top:0px; display:none;">
					<tbody>
						<tr>
							<th style="width:80px">브랜드</th>
							<th colspan="2" style="width:150px">상품명</th>
							<th>판매가</th>
							<th>사이즈 / 수량</th>
							<th>금액</th>
							<th> </th>
						</tr>
						<c:forEach var="item" items="${customerPack.items}">
							<tr id="item_${item.itemId}" class="item-container" data-id="${item.itemId}">
								<td><span class="item-customer">${item.product.brand.brandName}</span></td>
								<td class="item-image-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><img class="item-image" src="/image/products/${item.product.productImage}"></a></td>
								<td class="item-name-container"><a href="/shop/products/${item.product.productId}" style="text-decoration:none"><span class="item-name">${item.product.productName}</span></a></td>
								<td><span class="item-price">${item.product.productPrice}</span></td>
								<td class="quantity-container">
									<c:forEach var="quantity" items="${item.quantities}">
									<div>
										<span>${quantity.size}</span>
										<span class="quantity">${quantity.value}</span>
									</div>
									</c:forEach>
								</td>
								<td><span class="item-price">${item.price}</span></td>
								<td>
									<input type="button" class="reject" value="사입취소">
								</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot style="padding:5px 0">
						<tr class="extra-cost">
							<td colspan="2">
								<span>중량</span>
								<input class="order-weight" type="number" style="width:50px;" value="0"/><span>Kg</span>
							</td>
							<td>
								<span>배송비</span>
								<input class="ship-cost" type="number" style="width:50px;" value="0"/><span>원</span>
							</td>
							<td>
								<span>추가DC</span>
								<input class="extra-dc" type="number" style="width:50px;" value="0"/><span>원</span>
							</td>
							<td colspan="2">
								<span>청구액</span>
								<input class="origin-price" type="hidden" value=${customerPack.price} />
								<span class="order-price" style="font-weight:bold; font-size:15px;">${customerPack.price}</span><span>원</span>
							</td>
							<td>
								<button class="btn shipping-order"><i class="fa fa-truck"></i>  배송처리</button>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
			</c:forEach>	
			<div id="list-footer">
				<div> 대기주문 : <span id="wait-orders">0</span>
				</div>
				<div> 대기수량 : <span id="wait-quantities">0</span>
				</div>
				<div style="width:46%"> 총 주문금액 : <span id="wait-price">0</span>
				</div>
			</div>
		</div>
	</div>
	<script>
		var costPerWeight = ${costPerWeight};
		window.addEventListener('load',function() {
			refreshWaitValues();
			var weightInputs = document.querySelectorAll(".order-weight");
			for (var i = 0; i < weightInputs.length; i++) {
				weightInputs[i].addEventListener("keyup", InputOrderWeight, false);
			}
			var shippingCostInputs = document.querySelectorAll(".ship-cost");
			for (var i = 0; i < shippingCostInputs.length; i++) {
				shippingCostInputs[i].addEventListener("keyup", InputOrderShippingCost, false);
			}
			var extraDCInputs = document.querySelectorAll(".extra-dc");
			for (var i = 0; i < extraDCInputs.length; i++) {
				extraDCInputs[i].addEventListener("keyup", InputExtraDC, false);
			}
			var shippingOrderBtns = document.querySelectorAll(".shipping-order");
			for (var i = 0; i < shippingOrderBtns.length; i++) {
				shippingOrderBtns[i].addEventListener("click", shippingOrder, false);
			}
			var rejectBtns = document.querySelectorAll(".reject");
			for (var i = 0; i < rejectBtns.length; i++) {
				rejectBtns[i].addEventListener("click", rejectItem, false);
			}
			
		}, false);
		
		function refreshWaitValues() {
			var val;
			var orders = document.querySelectorAll(".table-container");
			document.querySelector("#wait-orders").innerHTML=orders.length;
			
			var totalQuantityContainer = document.querySelectorAll(".total-order-quantities");
			var totalQuantity = 0;
			for (var i = 0; i < totalQuantityContainer.length; i++) {
				totalQuantity += totalQuantityContainer[i].textContent * 1;
			}
			document.querySelector("#wait-quantities").innerHTML=totalQuantity;
			
			var totalPrices = document.querySelectorAll(".total-order-prices");
			var totalPrice = 0;
			for (var i = 0; i < totalPrices.length; i++) {
				val = totalPrices[i].textContent*1;
				totalPrice += val;
				totalPrices[i].textContent = val.toLocaleString().split(".")[0];
			}
			document.querySelector("#wait-price").innerHTML=totalPrice.toLocaleString().split(".")[0];
			
			var tableOpenBtn = document.querySelectorAll(".tableOpenBtn");
			for (var i = 0; i < tableOpenBtn.length; i++) {
				tableOpenBtn[i].addEventListener("click",function(e) {
					var table = e.target.parentNode.parentNode.parentNode.querySelector("table");
					if (table.style.display==="none") {
						table.style.display="table";
					} else {
						table.style.display="none";
					}
				},false);
			}
		}

		function InputOrderWeight(e) {
			var input = e.target.value;
			var newCost = input * costPerWeight;
			var shipCostInput = e.target.parentNode.parentNode.querySelector(".ship-cost");
			shipCostInput.value = newCost;
			calcOrderPrice(e.target.parentNodeSelector(".extra-cost"));
		}
		
		function InputOrderShippingCost(e) {
			calcOrderPrice(e.target.parentNodeSelector(".extra-cost"));
		}
		
		function InputExtraDC(e) {
			calcOrderPrice(e.target.parentNodeSelector(".extra-cost"));
		}
		
		function calcOrderPrice(rootElement) {
			var shipCost = rootElement.querySelector(".ship-cost").value*1;
			var extraDC = rootElement.querySelector(".extra-dc").value*1;
			var originPrice = rootElement.querySelector(".origin-price").value*1;
			
			rootElement.querySelector(".order-price").textContent = originPrice + shipCost - extraDC;
		}
		
		function shippingOrder(e) {
			var tableContainer = e.target.parentNodeSelector(".table-container");
			var shippingCost = tableContainer.querySelector(".ship-cost").value;
			var extraDC = tableContainer.querySelector(".extra-dc").value;
			var orderPrice = tableContainer.querySelector(".order-price").textContent;
			var itemContainers = tableContainer.querySelectorAll(".item-container");
			var json = "{";
			json += " items:[";
			for (var i = 0; i < itemContainers.length; i++) {
				if (i != 0) json += ", ";
				var itemId = itemContainers[i].getAttribute("data-id");
				json += "{itemId:"+itemId+"}";
			}	
			json +="],";
			json += " shippingCost:"+shippingCost+",";
			json += " extraDiscount:"+extraDC+",";
			json += " orderPrice:"+orderPrice+",";
			json += " paiedPrice:0,";
			json += " recallPrice:0}";
			console.log(json);
			ydbaobao.ajax({
				method: "post",
				url: "/admin/orders/shipping",
				param:"order="+json,
				success: function(req){
					if (req.responseText == "OK") {
						alert("배송처리 되었습니다.");
					} else {
						alert("배송처리에 실패했습니다.");
					}
				}
			});
		}
		
		function rejectItem(e) {
			alert("해당상품의 사입을 취소합니다.");
			var itemId = e.target.parentNodeSelector(".item-container").getAttribute("data-id");
			ydbaobao.ajax({
				method: "post",
				url: "/admin/orders/cancelaccept/"+itemId,
				success: function(req){
					if (req.responseText == "OK") {
						alert("사입이 취소되었습니다.");
						e.target.parentNodeSelector(".item-container").remove();
					} else {
						alert("사입 취소를 실패했습니다.");
					}
				}
			});
		}
		
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
