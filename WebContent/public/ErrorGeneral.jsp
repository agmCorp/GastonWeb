<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/reset.css" />
<link type="text/css" rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/structure.css" />
<link rel="shortcut icon" type="image/x-icon"
	href="${pageContext.request.contextPath}/resources/images/favicon.ico" />
<script src="${pageContext.request.contextPath}/resources/js/scripts.js"></script>
<style>
.box.login {
	height: 134px;
}
</style>
<title>Murphy</title>
</head>
<body onload="breakout()">
	<form class="box login" action="${pageContext.request.contextPath}">
		<fieldset class="boxBody">
			<center>
				<label>ups!...</label>
			</center>
		</fieldset>
		<footer> <input type="submit" class="btnLogin" value="volver"
			tabindex="1"> </footer>
	</form>
	<jsp:include page="/resources/common/JSPFooter.html" />
</body>
</html>