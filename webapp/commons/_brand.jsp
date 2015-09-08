<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="brand-selector" class="wrap content">
	<div id="brand-initial-tab">
		<label>BRANDS</label>
		<ul>
			<li class="first-letter"><span>#</span><li>
			<c:forEach var="firstLetter" items="${firstLetterList}">
				<li class="first-letter"><span>${firstLetter}</span></li>
			</c:forEach>
		</ul>
	</div>
	<div id="brand-list">
		<ul>
			<c:forEach var="brand" items="${brands}">
				<li><a href="/shop/brands/${brand.brandId}/products?page=1"><i class='fa fa-bookmark'></i>  <span>${brand.brandName}(${brand.brandCount})</span></a></li>
			</c:forEach>
		</ul>
	</div>	
</div>
<script src="/js/brand.js"></script>