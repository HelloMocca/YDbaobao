<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="brand-search-container">
	<div id="brand-search-bar" class="wrap content">
		<div id="search-bar-container">
			<a href="/"> <img width="180" height="55" src="/image/logo_5.png" style="margin-right: 90px; margin-left: 60px; margin-top: 10px;"/>
			</a>
			<form id="search-form">
				<div id="search-bar">
					<div id="select-area" style="float:left;">
						<select id="select" style="width:100px;">
							<option class="selected" value="NAME">NAME</option>
							<option value="BRAND">BRAND</option>
						</select>
					</div>
					<input id="search-terms" type="text" style="float:right"/>
				</div>
				<button id="search-btn" class="btn" type="submit">Search</button>
			</form>
		</div>
	</div>
</div>
<script>
	window.addEventListener('load', function() {
		document.querySelector('#select').addEventListener('change', function(e) {
			document.querySelector('.selected').classList.remove('selected');
			for(var i = 0 ; i < e.target.options.length; i++) {
				if(e.target.options[i].value === e.target.value) {
					e.target.options[i].classList.add('selected');
				}	
			}
		}, false);
		
		document.querySelector("#search-form").addEventListener('submit', function(e) {
			e.preventDefault();
			var selected = document.querySelector('.selected').value;
			var terms = document.querySelector('#search-terms').value;
			if(terms===""){
				return;
			}
			if(selected === 'NAME') {
				window.location.href = '/search/products?terms=' + terms + '&page=1';
			}
			if(selected === 'BRAND') {
				window.location.href = '/search/brands?terms=' + terms + '&page=1';
			}
		}, false);
	}, false);
</script>