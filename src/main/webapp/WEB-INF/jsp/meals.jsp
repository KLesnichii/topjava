<%@ page import="ru.javawebinar.topjava.util.DateUtil" %><%--
  Created by IntelliJ IDEA.
  User: Shmel
  Date: 20.04.2020
  Time: 17:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Meals</title>
</head>
<body>
<section>
    <div id="add">
        <a href="meals?action=add">Добавить</a>
    </div>
    <table id="customers">
        <tr>
            <th>
                Дата/Время
            </th>
            <th>
                Описание
            </th>
            <th>
                Калории
            </th>
            <th colspan="2">

            </th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tbody id="${meal.excess?"excessRed":"excessGreen"}">
            <tr>
                <td><%=DateUtil.dateFormat(meal.getDateTime())%>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?id=${meal.id}&action=delete">Удалить</a></td>
                <td><a href="meals?id=${meal.id}&action=edit">Обновить</a></td>
            </tr>
            </tbody>
        </c:forEach>
    </table>
</section>
</body>
</html>
