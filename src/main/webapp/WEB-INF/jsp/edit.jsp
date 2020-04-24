<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<%@ page import="ru.javawebinar.topjava.util.DateUtil" %><%--
  Created by IntelliJ IDEA.
  User: Shmel
  Date: 22.04.2020
  Time: 16:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>edit</title>
</head>
<body>
<section>
    <div id="edit">
        <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
            <input type="hidden" name="id" value="${meal.id}">
            <div>
                <label for="dateTime">Дата/Время</label>
                <input type="datetime-local" id="dateTime" name="dateTime"
                       value="${not empty meal.dateTime ? meal.dateTime : null}">
            </div>
            <div>
                <label for="description">Описание</label>
                <input type="text" id="description" name="description" value="${meal.description}"
                       placeholder="Описание">
            </div>
            <div>
                <label for="calories">Калории</label>
                <input type="number" id="calories" name="calories" value="${meal.calories !=0 ? meal.calories: null }"
                       placeholder="1000">
            </div>
            <div>
                <button type="submit">Сохранить</button>
                <button type="button" onclick="window.history.back()">Отменить</button>
            </div>
        </form>
    </div>
</section>
</body>
</html>
