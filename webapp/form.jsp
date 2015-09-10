<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="/css/main.css" />
<link rel="stylesheet" type="text/css" href="/css/font-awesome.min.css" />
<title>YDbaobao</title>
</head>
<body>
	<div id="header">
		<!-- 상단 navigator -->
		<%@ include file="./commons/_topNav.jsp"%>
	</div>

	<div id="main-container">
		<div id="first-section" class="wrap content" style="height: 635px;">
			<div class="joinForm">
				<c:set var="actionUrl" value="/shop/join" />
				<c:if test="${isUpdate}">
					<c:set var="actionUrl" value="/shop/customers/update" />
				</c:if>

				<form:form modelAttribute="customer" method="post" action="${actionUrl}">
					<c:set var="pageName" value="회원가입" />
					<c:if test="${isUpdate}">
						<c:set var="pageName" value="개인정보수정" />
					</c:if>
					<h1>${pageName}</h1>

					<label for="customerId">아이디</label>
					<c:choose>
						<c:when test="${isUpdate}">
							<form:input path="customerId" readonly="true" />
						</c:when>
						<c:otherwise>
							<form:input id="join-userId" path="customerId" />
						</c:otherwise>
					</c:choose>
							<span id="join-userId-message" class="errorMessage"></span>
					<br />
					<label for="customerName">이름</label>
					<form:input path="customerName" />
					<br />
					<label for="customerPassword">비밀번호</label>
					<input id="join-userPassword" type="password" name="customerPassword" required/>
					<span id="join-userPassword-message" class="errorMessage"></span>
					<br />
					<label for="customerAgainPassword">비밀번호확인</label>
					<input id="join-userRePassword" type="password" name="customerAgainPassword" required/>
					<span id="join-userRePassword-message" class="errorMessage"></span>
					<br />
					<label for="customerPhone">전화번호</label>
					<form:input path="customerPhone" />
					<br />
					<label for="customerEmail">이메일</label>
					<form:input id="join-userEmail" path="customerEmail" />
					<span id="join-userEmail-message" class="errorMessage"></span>
					<br />
					<label for="customerAddress">주소</label>
					<form:input path="customerAddress" />
					<br />
					<label style="width:initial" class="passwordError"></label>
					<c:if test="${not empty message}">
						<label style="width:initial" class="error">${message}</label>
						<br />
					</c:if>
					<c:if test="${not empty validMessages}">
						<c:forEach var="validMessage" items="${validMessages}">
							<label style="width:initial" class="error">${validMessage}</label>
							<br />
						</c:forEach>
					</c:if>
					<button id="join-submit" >확인</button>
				</form:form>
			</div>
		</div>
	</div>
	<div id="footer">
		<%@ include file="./commons/_footer.jsp"%>
	</div>
	<script>
	window.addEventListener('load', function() {
		//회원가입 유효성 이벤트 등록
		setEventListener();
	}, false);
	
	document.querySelector('#join-submit').addEventListener('click', function(e){
		var password = document.querySelector('#join-userPassword');
		var rePassword = document.querySelector('#join-userRePassword');
		if(password.value==="" || password.value===""){
			return;
		}
/* 		else if(password.value !== rePassword.value){
			document.querySelector('.passwordError').innerText="비밀번호가 일치하지 않습니다.";
			return;
		} */
		else {
			e.target.parentNode.submit();
		}
		
	}, false);
	
	var setEventListener = function() {
		// Email //
		//TOTO 유효성체크항목 추후 협의.
		//joinCheck.setEmailValidation("join-userEmail", "join-userEmail-message");
		// Name //
		var bUpdate = "${isUpdate}";
		if(bUpdate !== "true")
			joinCheck.setNameValidation("join-userId", "join-userId-message");
		// Password //
		joinCheck.setPasswordValidation("join-userPassword", "join-userPassword-message");
		joinCheck.setPasswordValidation("join-userRePassword", "join-userRePassword-message");
	}
	</script>
	<script type="text/javascript" src="/js/joinCheck.js"></script>
</body>
</html>