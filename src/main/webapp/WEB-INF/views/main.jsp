
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>QuoteOfTheDay</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href=<%=request.getContextPath()+"/css/main.css"%>>
    <script src="js/main.js" defer></script>
</head>
<body>
    <%@include file="header.jsp"%>
    <main>
        <div class="background-image" id="background"></div>

        <section class="quote-section" id="quote-section">
            <div class="quote">
                <blockquote>
                    "${randomQuote.text}"
                    <input class="random-quote-category" id="random-quote-category" value="${randomQuote.category}" type="hidden">
                    <footer>— ${randomQuote.author}</footer>
                </blockquote>
            </div>
        </section>
        <div class="all-quotes">
            <h1>Все цитаты</h1>
            <c:forEach var="quote" items="${allQuotes}">
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
            </c:forEach>
        </div>
    </main>
</body>
</html>
