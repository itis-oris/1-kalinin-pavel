<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>QuoteOfTheDay</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href=<%=request.getContextPath()+"/css/main.css"%>>
    <script src="js/main.js" defer></script>
</head>
<body>
<div>
    <%@ include file="header.jsp" %>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <p>${error}</p>
        </div>
    </c:if>
    <div class="all-quotes">
        <h1>Избранные цитаты</h1>
        <c:if test="${not empty likedQuotes}">
            <c:forEach var="quote" items="${likedQuotes}">
                <div class="quote-item">
                    <blockquote>
                        "${quote.text}"
                        <footer>— ${quote.author}</footer>
                    </blockquote>

                    <div class="quote-actions">
                        <div class="like-section">
                            <div class="like-container">
                                <img src="${quote.isLikedByUser() ? 'assets/heart-filled.png' : 'assets/heart.png'}" width="15" height="15" class="like-img" onclick="toggleLike(${quote.id}, this)">
                            </div>
                        </div>
                        <div class="category-section">
                            <span>#${quote.category}</span>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${empty likedQuotes}">
            <div class="no-liked-quotes">Здесь ничего нет</div>
        </c:if>
    </div>
</div>
</body>
</html>