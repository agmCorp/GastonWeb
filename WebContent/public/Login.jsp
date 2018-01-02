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
<title>Login</title>
</head>
<body onload="breakout()">
	<form class="box login" action="j_security_check" method="post">
		<fieldset class="boxBody">
			<label>Usuario <a
				href="${pageContext.request.contextPath}/public/UsuarioRecuperarRegistroActivacion.xhtml"
				class="rLink" tabindex="4">No recibí mail de activación</a></label><input type="text" tabindex="1"
				name="j_username" required> <label> Contraseña <a
				href="${pageContext.request.contextPath}/public/UsuarioRegistrar.xhtml"
				class="rLink" tabindex="5">Regístrese</a> <a
				href="${pageContext.request.contextPath}/public/UsuarioRecuperarPwd.xhtml"
				class="rLink" tabindex="6">¿Olvidó su contraseña?</a>
			</label> <input type="password" tabindex="2" name="j_password" required>
		</fieldset>
		<footer> <input type="submit" class="btnLogin" value="Login"
			tabindex="3"> </footer>
	</form>
	<jsp:include page="/resources/common/JSPFooter.html" />
</body>
</html>