<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cartList" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="totalPrice" type="java.math.BigDecimal" scope="request"/>
<tags:master pageTitle="Cart">
    <c:if test="${not empty cartList}">
        <div class="text-header">
            Cart
            <form method="post" class="inline">
                <button name="update" type="submit" class="update-button"></button>
            </form>
        </div>
        <br>
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
                <td></td>
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
                        <div class="text-body">
                            <form method="post" class="inline">
                                <button name="remove" type="submit" class="minus-icon" value="${cart.product.id}">
                                </button>
                            </form>
                                ${cart.quantity}
                            <form method="post" class="inline">
                                <button name="add" type="submit" class="plus-icon" value="${cart.product.id}">
                                </button>
                            </form>
                        </div>
                    </td>
                    <td class="price">
                        <fmt:formatNumber value="${cart.product.price * cart.quantity}" type="currency"
                                          currencySymbol="${cart.product.currency.symbol}"/>
                    </td>
                    <td>
                        <form  method="post" action="${pageContext.request.contextPath}/products/cart/deleteCartItem/${cart.product.id}">
                            <button name="delete" type="submit" class="cart-icon" value="${cart.product.id}">
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td class="price">
                    <fmt:formatNumber value="${totalPrice.doubleValue()}" type="currency"
                                      currencySymbol="${cartList.get(0).product.currency.symbol}"/>
                </td>
                <td></td>
            </tr>
        </table>
    </c:if>
    <c:if test="${empty cartList}">
        <div class="text-header">
            There are no products in the cart
            <form method="post" class="inline">
                <button name="update" type="submit" class="update-button"></button>
            </form>
        </div>
    </c:if>
</tags:master>
