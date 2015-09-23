<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${empty sessionAdmin}">
	<c:redirect url="/admin/check" />
</c:if>
<div id="menu">
	<div style="padding:15px 15px; color:#fff; background-color:#EA6576">관리자메뉴</div>
	<ul>
		<li><a href="/admin/customers?page=1"><span>회원관리</span></a></li>
		<li><a href="/admin/products/regist"><span>상품등록</span></a></li>
		<li><a href="/admin/products?page=1"><span>상품관리</span></a></li>
		<li><a href="/admin/brands"><span>브랜드관리</span></a></li>
		<li><a href="/admin/categories"><span>카테고리관리</span></a></li>
		<hr />
		<li><a href="/admin/orders/brands"><span>사입대기</span></a></li>
		<li><a href="/admin/orders/accepted"><span>배송대기</span></a></li>
		<li><a href="/admin/orders/customers"><span>회원별주문관리</span></a></li>
		<hr />
		<li><a href="/admin/indexImages"><span>첫화면관리</span></a></li>
		<li><a href="/admin/config"><span>관리자설정</span></a></li>
	</ul>
</div>    
