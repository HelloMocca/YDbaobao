<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Yuandamaoyi:: Welcome</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="/css/main.css" />
<link rel="stylesheet" type="text/css" href="/css/font-awesome.min.css" />
</head>
<body>
	<div id="main-container">
		<div id="header">
			<!-- 상단 navigator -->
			<%@ include file="./commons/_topNav.jsp"%>
			<!-- 브랜드/제품 검색바 -->
			<%@ include file="./commons/_search.jsp"%>
		</div>
		<div>
			<!-- 수평 카테고리 메뉴 -->
			<%@ include file="./commons/_horizontalCategory.jsp"%>
		</div>
		<div id="first-section" class="wrap content" style="position:relative; padding:25px 0;">
			<c:if test="${not empty isHome}">
				<div id="showcase">
					<img id="indexImage" style="height:100%;">
				</div>
			</c:if>
		</div>
		<div id="second-section" class="wrap content">
			<!-- 브랜드 탭(A~Z) 메뉴 -->
			<%@ include file="./commons/_brand.jsp" %>
			<div id="item-container" class="wrap content">
				<h2>최근 등록된 상품</h2>
				<%@ include file="./commons/_productsBox.jsp" %>

				<%@ include file="./commons/_productListBar.jsp" %>
			</div>
		</div>
	</div>
	<div id="footer">
		<%@ include file="./commons/_footer.jsp"%>
	</div>
	<c:if test="${not empty indexImages}">
		<script>
		var indexImages = ${indexImages};
		var imgPathsLength = indexImages.length;
		var imgIndex = 0;
		var imgEl;
 		window.addEventListener("load", function() {
			imgEl = document.querySelector("#indexImage");
 			if(imgPathsLength === 0) {
 				imgEl.remove();
 				return;
 			}
 			imgEl.src = "/image/index/"+indexImages[imgIndex].indexImageName;
			setInterval(function(){
				addIndex();
				fadeOut(imgEl);
			}, 5000);
		}, false);
 		
 		function addIndex() {
 			if(imgIndex >= imgPathsLength-1)
 				imgIndex=0;
 			else
 				imgIndex++;
 		}
 		
		function fadeIn(id) {
			var level = 0;
			var inTimer = null;
			inTimer = setInterval( function(){ level = fadeInAction(id, level, inTimer); }, 50 );
		}

		function fadeInAction(id, level, inTimer) {
			level = level + 0.1;
			changeOpacity(id, level);
			if(level > 1) 
			{
				clearInterval(inTimer);
			}
			return level;
		}

		function fadeOut(id) {
			var level = 1;
			var outTimer = null;
			outTimer = setInterval( function(){ level = fadeOutAction(id, level, outTimer); }, 50 );
		}
		
		function fadeOutAction(id, level, outTimer) {
			level = level - 0.1;
			changeOpacity(id, level);
			if(level < 0) 
			{
				clearInterval(outTimer);
				imgEl.src = "/image/index/"+indexImages[imgIndex].indexImageName;
				fadeIn(imgEl);
			}
			return level;
		}

		function changeOpacity(obj,level) {
			obj.style.opacity = level; 
			obj.style.MozOpacity = level; 
			obj.style.KhtmlOpacity = level;
			obj.style.MsFilter = "'progid:DXImageTransform.Microsoft.Alpha(Opacity=" + (level * 100) + ")'";
			obj.style.filter = "alpha(opacity=" + (level * 100) + ");"; 
		}
		</script>
	</c:if>
	<script src="/js/ydbaobao.js"></script>
</body>
</html>
