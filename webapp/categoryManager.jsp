<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::카테고리관리</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<style>
	#categories td {
		text-align:center;
	}
	#categories input[type='text'] {
		width:300px;
		font-size:20px;
	}
</style>
</head>
<body>
	<div id="header" style="width: 100%;">
		<%@ include file="./_adminTopNav.jsp"%>
	</div>
	<div id="container">
		<%@ include file="./_adminNav.jsp" %>
		<div id="content">
			<h1>카테고리 관리</h1>
			<table id="categories" style="width: 500px;">
				<tr>
					<th style="width:50px">구분</th>
					<th >카테고리명</th>
					<th style="width:80;">선택</th>
				</tr>
				<c:forEach var="category" varStatus="status" items="${categories}">
					<tr id="${category.categoryId}">
						<td>${status.index+1}</td>
						<td><input type="text" value="${category.categoryName}" data-id="${category.categoryId}"></td>
						<td>
							<button class="update-category-btn">수정</button>
							<button class="delete-category-btn">삭제</button>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td></td>
					<td><input id="new-category" type="text"></td>	
					<td>
						<button class="create-category-btn">추가</button>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<script>
		window.addEventListener('load', function() {
			setCategoryEvent();
		}, false);

		function setCategoryEvent() {
			var updateBtn = document.querySelectorAll('.update-category-btn');
			var deleteBtn = document.querySelectorAll('.delete-category-btn');
			var createBtn = document.querySelector('.create-category-btn');
	
			// for문 조건문용 변수
			var length;

			for(var i = 0, length = updateBtn.length; i < length; i++) {
				updateBtn[i].addEventListener('click', function(e) {
					updateCategory(e);
				}, false);
			}

			for(var j = 0, length = deleteBtn.length; j < length; j++) {
				deleteBtn[j].addEventListener('click', function(e) {
					deleteCategory(e);
				}), false;
			}

			createBtn.addEventListener('click', createCategory, false);
		}

		function updateCategory(e) {
			var categoryId = e.target.parentElement.parentElement.id;
			var categoryName = document.querySelector('input[data-id="' + categoryId + '"]').value;

			if(categoryName === '') {
				alert('카테고리 명을 입력해주세요');
				return;
			}

			ydbaobao.ajax({
				method:'put',
				url:'/admin/categories/' + categoryId + '?categoryName=' + categoryName,
				success: function(req) {
					if(req.responseText === 'success') {
						alert('카테고리가 수정되었습니다')
					}
				}
			});
		}

		function deleteCategory(e) {
			var categoryId = e.target.parentElement.parentElement.id;
			if(confirm('정말 카테고리를 삭제하시겠습니까?') === true) {
				ydbaobao.ajax({
					method:'delete',
					url:'/admin/categories/' + categoryId,
					success: function(req) {
						if(req.responseText === 'success') {
							document.getElementById(categoryId).remove();
						}
					}
				});
				e.target.removeEventListener('click', deleteCategory, false);
			}
		}

		function createCategory() {
			var categoryName = document.querySelector('#new-category').value;

			if(categoryName === '') {
				alert("카테고리 명을 입력해주세요");
				return;
			}

			ydbaobao.ajax({
				method:'post',
				url:'/admin/categories',
				param: 'categoryName=' + categoryName,
				success: function(req) {
					if(req.responseText === 'success') {
						alert('카테고리가 추가되었습니다');
						location.reload();
					}
				}
			});
		}
	</script>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>