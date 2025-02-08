<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>Регистрация</title>
    <link rel="stylesheet" type="text/css" href=<%=request.getContextPath()+"/css/main.css"%>>
</head>
<body>
<div class="body-page-signup">
    <div class="body-signup">
        <div class="container-signup">
            <h1 class="title-sigup">Регистрация</h1>
            <form id="signupForm" class="create-form" action="<c:url value= "/signup"/>" method="POST">
                <div class="create-form-group">
                    <label class="control-label col-sm-3" for="name">Ваше имя</label>
                    <div class="controls col-sm-9">
                        <input id="name" name="name" class="create-form-control" type="text" value="" required/>
                    </div>
                    <c:if test="${not empty errorName}">
                        <p style="color: red;">${errorName}</p>
                    </c:if>
                </div>
                <div class="create-form-group">
                    <label class="control-label col-sm-3" for="login">E-mail</label>
                    <div class="controls col-sm-9">
                        <input id="login" name="login" class="create-form-control" type="text" value="" required/>
                    </div>
                </div>
                <div class="create-form-group">
                    <label class="control-label col-sm-3" for="password">Пароль</label>
                <div class="controls col-sm-9">
                    <input id="password" name="password" class="create-form-control" type="password" value="" required/>
                </div>
                    <c:if test="${not empty errorPassword}">
                        <p style="color: red;">${errorPassword}</p>
                    </c:if>
                </div>
                <button type="submit" class="btn btn-signup">Зарегистрироваться</button>
                <c:if test="${not empty errorEmail}">
                    <p style="color: red;">${errorEmail}</p>
                </c:if>
                <p>Уже зарегистрированы? <a class="link-signup" href="<c:url value="/signin"/>">Войдите</a></p>
            </form>
        </div>
    </div>
</div>
</body>
</html>
