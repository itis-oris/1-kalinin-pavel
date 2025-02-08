<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" type="text/css" href=<%=request.getContextPath()+"/css/main.css"%>>
  <script src="js/main.js" defer></script>
  <title>Викторина</title>
</head>
<body>
<%@include file="header.jsp"%>
<div class="quiz-body">
  <div class="quiz-container">
    <c:if test="${not empty errorQuiz}">
      <div class="error">${errorQuiz}</div>
    </c:if>

    <c:if test="${not empty quizQuote}">
      <div class="question">${quizQuote.text}</div>
      <input type="hidden" id="correctOption" value="${quizQuote.correctOption}">
      <div id="options">
        <c:forEach var="option" items="${quizQuote.options}">
          <div class="option" data-option="${option}">${option}</div>
        </c:forEach>
      </div>
      <button class="next-button" id="nextButton" onclick="window.location.reload()">Следующий вопрос</button>
    </c:if>
  </div>
</div>
</body>
</html>
