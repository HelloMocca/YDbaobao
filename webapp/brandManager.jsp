<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::브랜드관리</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<style>
#brand-table input[type='text'], input[type='text'] {
	width:25px;
	margin-right:5px;
}

#brand-table td{
	font-size:9px;
	text-align:center;
}

#brand-table input.brandName {
	width: 150px;
	font-size: 15px;
}

#brand-table .brand_sizes input{
	width:100px;
}

.discount_table input[type="number"] {
  width: 23px;
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
			<h1>브랜드 관리</h1>
			<input id="search-brand" type="text" style="width:150px;">
			<button class="search-brand-btn">검색</button>
			<table id="brand-table" style="width: 780px;">
				<tr>
					<th style="width:30px">#</th>
					<th style="width:150px">브랜드명</th>
					<th style="width:320px">할인율</th>
					<th style="width:100px">사이즈</th>
					<th style="">수량</th>
					<th style="width:80px">선택</th>
				</tr>
				<c:forEach var="brand" varStatus="status" items="${brands}">
					<tr id="brand_${brand.brandId}">
						<td>${status.index+1}</td>
						<td><input class="brandName" type="text" value="${brand.brandName}"
							data-id="${brand.brandId}"></td>
						<td class='discount_table'>
							1등급:<input min="0" max="100" type="number" value="${brand.discount_1}">
							2등급:<input min="0" max="100" type="number" value="${brand.discount_2}">
							3등급:<input min="0" max="100" type="number" value="${brand.discount_3}">
							4등급:<input min="0" max="100" type="number" value="${brand.discount_4}">
							5등급:<input min="0" max="100" type="number" value="${brand.discount_5}">
						</td>
						<td class='brand_sizes'>
							<input type="text" value="${brand.brandSize}">
						</td>
						<td class='brand_count'>
							${brand.brandCount}
						</td>
						<td>
							<button class="update-brand-btn">수정</button>
							<button class="delete-brand-btn">삭제</button>
						</td>
					</tr>
				</c:forEach>
				<tr id="new-brand">
					<td></td>
					<td><input class="brandName" id="new-brandName" type="text"></td>
					<td class='discount_table'>
						1등급:<input min="0" max="100" type="number" value="0">
						2등급:<input min="0" max="100" type="number" value="0">
						3등급:<input min="0" max="100" type="number" value="0">
						4등급:<input min="0" max="100" type="number" value="0">
						5등급:<input min="0" max="100" type="number" value="0">
					</td>
					<td class='brand_sizes'><input type="text"
						value="${brand.brandSize}"></td>
					<td></td>
					<td>
						<button class="create-brand-btn">추가</button>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<script>
		window.addEventListener('load', function() {
			setBrandEvent();
		}, false);

		function setBrandEvent() {
			var updateBtn = document.querySelectorAll('.update-brand-btn');
			var deleteBtn = document.querySelectorAll('.delete-brand-btn');
			var createBtn = document.querySelector('.create-brand-btn');
			var searchBtn = document.querySelector('.search-brand-btn');

			// for문 조건문용 변수
			var length;

			for (var i = 0, length = updateBtn.length; i < length; i++) {
				updateBtn[i].addEventListener('click', function(e) {
					updateBrand(e);
				}, false);
			}

			for (var j = 0, length = deleteBtn.length; j < length; j++) {
				deleteBtn[j].addEventListener('click', function(e) {
					deleteBrand(e);
				}), false;
			}

			createBtn.addEventListener('click', createBrand, false);
			searchBtn.addEventListener('click', searchBrand, false);

		}

		function updateBrand(e) {
			var brandId = e.target.parentElement.parentElement.id.split("brand_")[1];
			var brandName = document.querySelector('input[data-id="' + brandId + '"]').value;
			var discountTable = document.querySelectorAll('#brand_'+brandId+' .discount_table input');
			var value = 0;
			var param = "brandName="+brandName+"&discount_1="+discountTable[0].value;

			if(brandName === '') {
				alert('브랜드 명을 입력해주세요');
				return;
			}

			for (var i = 1; i < discountTable.length; i++) {
				value = discountTable[i].value*1;
				if (value > 100 || value < 0){
					value = 0;
					document.querySelector("#brand_"+brandId+" .discount_table input:nth-child("+(i+1)+")").value = 0;
				}
				param += "&discount_"+(i+1)+"="+value;
			}
			var brandSize = document.querySelector("#brand_"+brandId+" .brand_sizes input").value;
			param += "&brandSize="+brandSize;
			ydbaobao.ajax({
				method : 'post',
				url : '/admin/brands/' + brandId,
				param : param,
				success : function(req) {
					alert('브랜드 정보가 수정되었습니다')
				}
			});
		}

		function deleteBrand(e) {
			var brandId = e.target.parentElement.parentElement.id.split('_')[1];

			if (confirm('정말 브랜드를 삭제하시겠습니까?') === true) {
				ydbaobao.ajax({
					method : 'delete',
					url : '/admin/brands/' + brandId,
					success : function(req) {
						document.getElementById('brand_' + brandId).remove();
					}
				});
				e.target.removeEventListener('click', deleteBrand, false);
			}
		}

		function createBrand() {
			var brandName = document.querySelector('#new-brandName').value;
			var discountTable = document.querySelectorAll('#new-brand .discount_table input');
			var value = 0;
			var param = "brandName="+brandName+"&discount_1="+discountTable[0].value;

			if(brandName === '') {
				alert('브랜드 명을 입력해주세요');
				return;
			}

			for (var i = 1; i < discountTable.length; i++) {
				value = discountTable[i].value*1;
				if (value > 100 || value < 0){
					value = 0;
					document.querySelector("#new-brand .discount_table input:nth-child("+(i+1)+")").value = 0;
				}
				param += "&discount_"+(i+1)+"="+value;
			}

			var brandSize = document.querySelector("#new-brand .brand_sizes input").value;
			param += "&brandSize="+brandSize;

			ydbaobao.ajax({
				method : 'post',
				url : '/admin/brands/',
				param : param,
				success : function(req) {
					var signal = req.responseText;
					if (signal === "fail") {
						alert('브랜드명이 중복되었습니다');
					} else {
						alert('브랜드가 추가되었습니다');
						location.reload();
					}
				}
			});
		}

		function searchBrand() {
			var searchValue = document.querySelector('#search-brand').value;
			if(searchValue===""){
				return;
			}
			ydbaobao.ajax({
				method : 'get',
				url : '/admin/brands/find?searchValue=' + searchValue,
				success : function(req) {
					appendBrandList(JSON.parse(req.responseText));
				}
			});
		}

		function appendBrandList(json) {
			var table = document.querySelector('#brand-table');
			while (table.rows.length > 2)
				table.deleteRow(1);
			var brandsLength = json.length;
			var cell;
			for (var i = 0; i < brandsLength; i++) {
				row = table.insertRow(1);
				row.id = "brand_"+json[i].brandId;
				row.insertCell(0).innerHTML = '<td>' + (i + 1) + '</td>';
				row.insertCell(1).innerHTML = '<td><input class="brandName" type="text" value="' + json[i].brandName + '" data-id="' + json[i].brandId + '"></td>';
				cell = row.insertCell(2);
				cell.className = 'discount_table';
				cell.innerHTML = '<td>1등급:<input type="number" min="0" max="100" value="' + json[i].discount_1 + '">2등급:<input type="number" min="0" max="100" value="' + json[i].discount_2 + '">3등급:<input type="number" min="0" max="100" value="' + json[i].discount_3 + '">4등급:<input type="number" min="0" max="100" value="' + json[i].discount_4 + '">5등급:<input type="number" min="0" max="100" value="' + json[i].discount_5 + '"></td>';
				cell = row.insertCell(3);
				cell.className = 'brand_sizes';
				cell.innerHTML = '<td class="brand_sizes"><input type="text" value="' + json[i].brandSize + '"></td>';
				row.insertCell(4).innerHTML = '<td>' + json[i].brandCount + '</td>';
				row.insertCell(5).innerHTML = '<td><button class="update-brand-btn">수정</button><button class="delete-brand-btn">삭제</button></td>';
			}
			setBrandEvent();
		}
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
