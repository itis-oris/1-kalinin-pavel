<%--
  Created by IntelliJ IDEA.
  User: pavelkalinin
  Date: 02.11.2024
  Time: 21:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quote</title>
    <link rel="stylesheet" type="text/css" href=<%=request.getContextPath()+"/css/main.css"%>>
    <script src="js/main.js" defer></script>
</head>
<body>

<%@include file="header.jsp"%>
<div class="my-body">
  <h1 class="my-title">Создать цитату</h1>
  <div class="my-form-body">
    <div class="my-form-container">
      <form id="newQuoteForm" class="form-horizontal" action="<c:url value= "/add_new_quote"/>" method="POST">
        <div class="form-group">
          <label class="control-label col-sm-3" for="quoteText">Текст</label>
          <div class="controls col-sm-9">
            <textarea id="quoteText" name="quoteText" class="form-control" required></textarea>
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3" for="quoteAuthor">Автор</label>
          <div class="controls col-sm-9">
            <input id="quoteAuthor" name="quoteAuthor" class="form-control" type="text" value="" required/>
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3" for="quoteCategory">Категория</label>
          <div class="controls col-sm-9">
            <select id="quoteCategory" name="quoteCategory" class="form-control" required>
              <c:forEach var="category" items="${categories}">
                <option value="${category.id}">${category.name}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <button type="submit">Добавить</button>
      </form>
    </div>
  </div>

  <c:if test="${not empty param.state}">
    <div class="alert">
      <c:if test="${param.state == 'error'}">
        <p style="color:red;">Ошибка добавления цитаты</p>
      </c:if>
      <c:if test="${param.state == 'success'}">
        <p style="color:green;">Цитата успешно добавлена</p>
      </c:if>
    </div>
  </c:if>

  <c:if test="${not empty myQuotes}">
    <h1>Мои цитаты</h1>
    <div class="my-quotes-container">
      <ul class="quote-list">
        <c:forEach var="quote" items="${myQuotes}">
          <div class="my-quote-item">
            <div class="quote-item">
              <blockquote>
                "${quote.text}"
                <footer>— ${quote.author}</footer>
              </blockquote>

              <div class="quote-actions">
                <div class="like-section">
                  <div class="like-container">
                    <img src="${quote.isLikedByUser() ? 'assets/heart-filled.png' : 'assets/heart.png'}" width="15" height="15" class="like-img" onclick="toggleLike(${quote.id}, this)">
                    <span class="like-count">${quote.rating}</span>
                  </div>
                </div>
                <div class="category-section">
                  <span>#${quote.category}</span>
                </div>
              </div>
            </div>
            <form action="<c:url value='/delete_quote'/>" method="POST" style="display:inline;">
              <input type="hidden" name="quoteId" value="${quote.id}"/>
              <button type="submit">Удалить</button>
            </form>
            <a href="<c:url value="/updateQuote?id=${quote.id}"/>">Изменить</a>
          </div>
        </c:forEach>
      </ul>
    </div>
  </c:if>
</div>
</body>
</html>
