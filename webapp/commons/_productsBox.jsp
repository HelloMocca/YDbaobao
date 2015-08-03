<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${not empty products}">
		<ul>
			<c:forEach var="product" items="${products}" varStatus="status">
				<li class="item">
					<div align="center">
						<a href="/shop/products/${product.productId}"> 
							<img src="/image/products/${product.productImage}" />
							<div class="item-info">
								<div class="item-desc">
									<c:out value="${product.brand.brandName}" />
								</div>
								<div class="item-name">
									<c:out value="${product.productName}" />
								</div>
								<c:choose>
									<c:when test="${product.isSoldout == 1}">
										<div class="item-soldout">
											<c:out value="품절" />
										</div>
									</c:when>
									<c:otherwise>
										<div class="item-price">
											<c:out value="${product.productPrice}" />
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</a>
					</div>
				</li>
			</c:forEach>
		</ul>
		<script>
			var el = document.querySelectorAll('.item-price');
			var length = el.length;
		
			for(var i = 0; i < length; i++) {
				el[i].textContent = parseInt(el[i].textContent).toLocaleString().split(".")[0];
			}
		</script>
	</c:when>
	<c:otherwise>
		<h1 class="noItem">등록된 상품이 없습니다.</h1>
	</c:otherwise>
</c:choose>