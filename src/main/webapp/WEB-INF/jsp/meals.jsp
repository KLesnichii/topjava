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
    <link rel="stylesheet" href="css/style">
    <title>Meals</title>
</head>
<body>
<section>
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
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <c:if test="${meal.excess}">
                <tbody id="excessRed">
            </c:if>
            <c:if test="${!meal.excess}">
                <tbody id="excessGreen">
            </c:if>
            <tr>
                <td><%=DateUtil.dateFormat(meal.getDateTime())%></td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
            </tbody>
        </c:forEach>
    </table>
</section>
</body>
</html>
