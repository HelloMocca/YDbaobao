<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>관리자페이지::회원관리</title>
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
		<%@ include file="./_adminNav.jsp" %>
		<div id="content">
			<h1>회원상세보기</h1>
			<table id="member-table" style="width: 800px;">
				<tr>
					<th>회원아이디</th>
					<td>${customer.customerId}</td>
				</tr>
				<%-- <tr>
					<th>비밀번호</th>
					<td>${customer.customerPassword}</td>
				</tr> --%>
				<tr>
					<th>회원이름</th>
					<td>${customer.customerName}</td>
				</tr>
				<tr>
					<th>이메일</th>
					<td>${customer.customerEmail}</td>
				</tr>
				<tr>
					<th>등급</th>
					<td>
						<select id="gradeSelector">
							<option name="0" value="0">0</option>
							<option name="1" value="1">1</option>
							<option name="2" value="2">2</option>
							<option name="3" value="3">3</option>
							<option name="4" value="4">4</option>
							<option name="5" value="5">5</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>가입일</th>
					<td>${customer.customerCreateDate}</td>
				</tr>
				<tr>
					<th>주소</th>
					<td>${customer.customerAddress}</td>
				</tr>
				<tr>
					<th>핸드폰</th>
					<td>${customer.customerPhone}</td>
				</tr>
			</table>
		</div>
	</div>
</body>
<script>
	var selectedGrade = ${customer.customerGrade};
	var customerId = '${customer.customerId}';
	window.addEventListener('load', function(){
		document.querySelector('#gradeSelector option[name="' + selectedGrade + '"]').selected = 'selected';
		document.querySelector('#gradeSelector').addEventListener('change', function(e){
			if (selectedGrade != e.target.value) {
				ydbaobao.ajax({
					method:'put',
					url:'/admin/customers/' + customerId + '?grade=' + e.target.value,
					success: function(req){
						if(req.responseText === 'success') {
							alert('회원 등급이 변경되었습니다');
						}
					}
				});
				selectedGrade = e.target.value;
			}
		}, false);
	}, false);
</script>
<script src="/js/ydbaobao.js"></script>
</html>