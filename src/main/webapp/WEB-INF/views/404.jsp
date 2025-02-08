<%--
  Created by IntelliJ IDEA.
  User: pavelkalinin
  Date: 27.10.2024
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Страница не найдена</title>
    <link rel="stylesheet" type="text/css" href=<%=request.getContextPath()+"/css/404.css"%>>
</head>
<body>
    <div class="container">
        <div class="content">
            <img src="<%= request.getContextPath() + "/assets/wolf404.jpg"%>" alt="Error Image" class="error-image"/>
            <div class="text-container">
                <span class="error-text">404</span>
                <span class="error-description"> Oops, page not found</span>
            </div>
        </div>
    </div>
</body>
</html>
