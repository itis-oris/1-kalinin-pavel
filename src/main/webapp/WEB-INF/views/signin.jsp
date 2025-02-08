<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--
  Created by IntelliJ IDEA.
  User: pavelkalinin
  Date: 29.10.2024
  Time: 16:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Вход</title>
    <link rel="stylesheet" type="text/css" href=<%=request.getContextPath()+"/css/main.css"%>>

</head>
<body>
<div class="body-page-signin">
  <div class="body-signin">
    <div class="container-signin">
      <h1 class="title-sigin">Вход</h1>
      <form id="signinForm" class="create-form" action="<c:url value= "/signin"/>" method="POST">
        <div class="create-form-group">
          <label class="control-label col-sm-3" for="username">E-mail</label>
          <div class="controls col-sm-9">
            <input id="username" name="username" class="create-form-control" type="text" value=""/>
          </div>
        </div>
        <div class="create-form-group">
          <label class="control-label col-sm-3" for="password">Password</label>
          <div class="controls col-sm-9">
            <input id="password" name="password" class="create-form-control" type="password" value=""/>
          </div>
        </div>
        <div class="create-form-group">
          <label>Запомнить меня: <input type="checkbox" name="rememberMe" checked></label>
        </div>
        <button type="submit" class="btn btn-add">Войти</button>
      </form>
      <c:if test="${param.error == 'empty'}">
        <p style="color:red;">Пожалуйста, заполните все поля.</p>
      </c:if>

      <c:if test="${param.error == 'invalid'}">
        <p style="color:red;">Неверный email или пароль. Попробуйте еще раз.</p>
      </c:if>
      <a class="link-signin" href="<c:url value="/signup"/>">Нет аккаунта?</a>
    </div>
    <div>
      <img src="assets/login.jpg" class="signin-img">
    </div>
  </div>
</div>
</body>
</html>
