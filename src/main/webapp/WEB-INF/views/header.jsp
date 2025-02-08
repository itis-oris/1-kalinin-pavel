<%@ page import="entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<header>
  <h1 class="header-name">Quote</h1>

  <div class="user-menu">
    <c:if test="${not empty user}">
    <div class="user-info">
      <button id="menuButton" class="menu-button">${user.name}</button>
      <div id="dropdownMenu" class="dropdown-content">
        <a href="<c:url value="/"/>">Главная</a>
        <a href="<c:url value="/quiz"/>">Играть</a>
        <a href="<c:url value="/liked"/>">Избранное</a>
        <a href="<c:url value="/myQuotes"/>">Создать цитату</a>
        <a href="<c:url value="/signout"/> ">Выйти</a>
      </div>
    </div>
    </c:if>
    <c:if test="${empty user}">
    <div class="user-info">
      <a href="<c:url value="/signin"/>">Войти</a>
    </div>
    </c:if>
  </div>
</header>
