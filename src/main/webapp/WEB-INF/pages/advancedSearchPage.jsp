<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Advanced search page">
    <h1>Advanced search page</h1>
    <form>
        <table class="no-border">
            <tr class="no-border">
                <td class="no-border">
                    Product code
                </td>
                <td class="no-border">
                    <input name="productCode" value="${param.productCode}"/>
                    <c:if test="${not empty productCodeError}">
                        <p class="red">
                                ${productCodeError}
                        </p>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td class="no-border">
                    Min price
                </td>
                <td class="no-border">
                    <input name="minPrice" value="${param.minPrice}"/>
                    <c:if test="${not empty minPriceError}">
                        <p class="red">
                                ${minPriceError}
                        </p>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td class="no-border">
                    Max price
                </td>
                <td class="no-border">
                    <input name="maxPrice" value="${param.maxPrice}"/>
                    <c:if test="${not empty maxPriceError}">
                        <p class="red">
                                ${maxPriceError}
                        </p>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td class="no-border">
                    Min stock
                </td>
                <td class="no-border">
                    <input name="minStock" value="${param.minStock}"/>
                    <c:if test="${not empty minStockError}">
                        <p class="red">
                                ${minStockError}
                        </p>
                    </c:if>
                </td>
            </tr>
        </table>
        <button name="search">Search</button>
    </form>
    <c:if test="${not empty products}">
        <p class="green">
            Found ${products.size()} products
        </p>
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
                </td>
            </tr>
            </thead>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>
                        <img class="product-tile" src="${product.imageUrl}" alt="Phone image">
                    </td>
                    <td>
                        ${product.description}
                    </td>
                    <td class="price">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</tags:master>