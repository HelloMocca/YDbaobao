<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html>
<head>
<title>관리자페이지::상품등록</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="/css/admin.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
</head>
<body>
	<div id="header" style="width: 100%;">
		<%@ include file="./_adminTopNav.jsp"%>
	</div>
	<div id="container">
		<%@ include file="./_adminNav.jsp"%>
		<div id="content">
			<h1>상품 등록</h1>
			<div id="imageFiles">
				<form:form class="imageUploadForm" method="post" action="/admin/products" modelAttribute="product" enctype="multipart/form-data">
					<label class="control-label">브랜드 선택 :</label>
					<form:select path="brand.brandId">
						<c:forEach var="brand" items="${brandList}">
							<form:option value="${brand.brandId}" label="${brand.brandName}" />
						</c:forEach>
					</form:select>
					<br />
					<label class="control-label">이미지 선택 :</label>
					<input class="imageFile" type="file" name="imageFile" accept="image/x-png, image/gif, image/jpeg" multiple />
					<button type="button" class='submitBtn' disabled='true'>저장</button>
				</form:form>
				<div class="imageList"></div>
			</div>
		</div>
	</div>

	<script>

		var imageFile = document.body.querySelector('.imageFile');
		imageFile.addEventListener('change', function(e) {
			appendImageList();
			disableSubmitBtn();
		}, false);

		function appendImageList() {
			refreshImageList();
			var el = undefined;
			var selectedFileList = document.body.querySelector('.imageFile').files;
			for (var i = 0; i < selectedFileList.length; i++) {
				el = document.createElement('li');
				el.setAttribute('class', 'file-list');
				el.setAttribute('value', selectedFileList[i].name);
				el.innerHTML = selectedFileList[i].name;
				document.body.querySelector('.imageList').appendChild(el);
			}
		}

		function refreshImageList() {
			document.body.querySelector('.imageList').innerHTML = '';
		}

		function disableSubmitBtn() {
			if (document.body.querySelector('.imageFile').files.length > 0) {
				document.body.querySelector('.submitBtn').disabled = false;
			}
			if (document.body.querySelector('.imageFile').files.length === 0) {
				document.body.querySelector('.submitBtn').disabled = true;
			}
		}
		
		document.body.querySelector('.submitBtn').addEventListener('click', function(e){
			e.target.parentElement.submit();
			var imgCount = document.body.querySelector('.imageFile').files.length;
			var message = imgCount + '개의 상품이 등록되었습니다.';
			alert(message);
		}, false);
	</script>
</body>
</html>
