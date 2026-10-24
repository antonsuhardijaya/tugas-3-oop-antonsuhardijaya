<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - Aplikasi Kasir</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-wrap">
    <form class="login-box" method="post" action="${pageContext.request.contextPath}/login">
        <h1>🧾 Aplikasi Kasir</h1>
        <p>Silakan login untuk melanjutkan</p>
        <c:if test="${not empty error}"><div class="msg err"><c:out value="${error}"/></div></c:if>
        <label for="username">Username</label>
        <input id="username" name="username" value="<c:out value='${username}'/>" required autofocus autocomplete="username">
        <label for="password">Password</label>
        <input id="password" type="password" name="password" required autocomplete="current-password">
        <button class="btn" type="submit">Login</button>
    </form>
</div>
</body>
</html>
