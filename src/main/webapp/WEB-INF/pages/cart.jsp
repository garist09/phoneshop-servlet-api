<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cartList" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Cart">
    <c:if test="${not empty cartList}">
        <h1>Cart</h1>
        <table>
            <tr>
                <td>
                    Image
                </td>
                <td>
                    Description
                </td>
                <td>
                    Price
                </td>
                <td>
                    Quantity
                </td>
                <td>
                    Total price
                </td>
            </tr>
            <c:forEach var="cart" items="${cartList}">
                <tr>
                    <td>
                        <img class="product-tile" src="${cart.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${cart.product.id}">
                                ${cart.product.description}
                        </a>
                    </td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${cart.product.id}">
                            <fmt:formatNumber value="${cart.product.price}" type="currency"
                                              currencySymbol="${cart.product.currency.symbol}"/>
                        </a>
                    </td>
                    <td>
                            ${cart.quantity}
                    </td>
                    <td class="price">
                        <fmt:formatNumber value="${cart.product.price * cart.quantity}" type="currency"
                                          currencySymbol="${cart.product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty cartList}">
        <h2>
            There are no products in the cart
        </h2>
    </c:if>
</tags:master>
