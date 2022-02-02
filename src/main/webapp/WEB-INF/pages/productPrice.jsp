<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="currentDate" type="java.lang.String" scope="request"/>
<tags:master pageTitle="Product Price">
    <h1>Price history</h1>
    <p>
    ${product.description}
    </p>
    <table>
        <tr>
            <td>Start date</td>
            <td>Price</td>
        </tr>
        <c:forEach var="pair" items="${product.priceHistory}">
            <tr>
                <td>${pair.key}</td>
                <td>
                    <fmt:formatNumber value="${pair.value}" type="currency" currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td>${currentDate}</td>
            <td>
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
    </table>
</tags:master>