<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${not empty sessionCustomer}">
	</c:when>
	<c:when test="${not empty sessionAdmin }">
	</c:when>
	<c:otherwise>
		<c:redirect url="./shop/loginForm" />
	</c:otherwise>
</c:choose>
<div id="horizontal-category-menu">
	<ul>
		<li><a href="/shop/products/?page=1">전체상품</a></li>
		<c:forEach var="category" items="${categories}" varStatus="status">	
			<li><a href="/shop/categories/${category.categoryId}/products?page=1">
				<span><c:out value="${category.categoryName}"/></span>
			</a></li>
		</c:forEach>
	</ul>
</div>