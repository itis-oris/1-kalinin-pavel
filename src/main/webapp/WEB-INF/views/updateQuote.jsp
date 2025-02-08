<%--
  Created by IntelliJ IDEA.
  User: pavelkalinin
  Date: 13.12.2024
  Time: 12:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Обновить цитату</title>
    <link rel="stylesheet" type="text/css" href=<%=request.getContextPath()+"/css/main.css"%>>
    <script src="js/main.js" defer></script>
</head>
<body>
<%@include file="header.jsp"%>
<div class="my-body">
    <h1 class="my-title">Обновить цитату</h1>
    <div class="my-form-body">
        <div class="my-form-container">
            <form id="newQuoteForm" class="form-horizontal" action="<c:url value= "/updateQuote"/>" method="POST">
                <div class="form-group">
                    <label class="control-label col-sm-3" for="quoteText">Текст</label>
                    <div class="controls col-sm-9">
                        <textarea id="quoteText" name="quoteText" class="form-control" required>${updatingQuote.text}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-3" for="quoteAuthor">Автор</label>
                    <div class="controls col-sm-9">
                        <input id="quoteAuthor" name="quoteAuthor" class="form-control" type="text" value="${updatingQuote.author}" required/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-3" for="quoteCategory">Категория</label>
                    <div class="controls col-sm-9">
                        <select id="quoteCategory" name="quoteCategory" class="form-control" required>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.id}" <c:if test="${category.name == updatingQuote.category}">selected</c:if>>${category.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <input value="${updatingQuote.id}" name="quoteId" type="hidden">
                <button type="submit">Сохранить</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
