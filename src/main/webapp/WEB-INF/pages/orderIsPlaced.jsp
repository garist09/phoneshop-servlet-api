<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Order is placed">
    <c:if test="${not empty orderList}">
        <div class="text-header">
            Orders
        </div>
        <br>
        <c:forEach var="order" items="${orderList}">
            <div>
                <div class="text-body-left">
                    Order number: ${order.id.substring(0, 18)}
                    <form method="post" class="inline">
                        <button name="deleteOrderId" type="submit" class="delete-order-icon" value="${order.id}">
                        </button>
                    </form>
                </div>
                <br>
                <table class="inline">
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
                    <c:forEach var="cartItem" items="${order.cartItemList}">
                        <tr>
                            <td>
                                <img class="product-tile" src="${cartItem.product.imageUrl}">
                            </td>
                            <td>
                                <a href="${pageContext.servletContext.contextPath}/products/${cartItem.product.id}">
                                        ${cartItem.product.description}
                                </a>
                            </td>
                            <td class="price">
                                <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${cartItem.product.id}">
                                    <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                                                      currencySymbol="${cartItem.product.currency.symbol}"/>
                                </a>
                            </td>
                            <td>
                                <div class="text-body">
                                        ${cartItem.quantity}
                                </div>
                            </td>
                            <td class="price">
                                <fmt:formatNumber value="${cartItem.product.price * cartItem.quantity}" type="currency"
                                                  currencySymbol="${cartItem.product.currency.symbol}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <c:set var="symbol" value="${orderList.get(0).cartItemList.get(0).product.currency.symbol}"/>
                <table class="inline">
                    <tr>
                        <td>
                            First name
                        </td>
                        <td>
                                ${order.firstName}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Last name
                        </td>
                        <td>
                                ${order.lastName}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Phone number
                        </td>
                        <td>
                                ${order.phoneNumber}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Delivery address
                        </td>
                        <td>
                                ${order.deliveryAddress}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Delivery date
                        </td>
                        <td>
                                ${order.deliveryDate}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Payment method
                        </td>
                        <td>
                                ${order.paymentMethod}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Subtotal
                        </td>
                        <td>
                            <fmt:formatNumber value="${order.subtotal}" type="currency"
                                              currencySymbol="${symbol}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Delivery cost
                        </td>
                        <td>
                            <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                              currencySymbol="${symbol}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Cart total
                        </td>
                        <td>
                            <fmt:formatNumber value="${order.cartTotal}" type="currency"
                                              currencySymbol="${symbol}"/>
                        </td>
                    </tr>
                </table>
            </div>
            <br>
        </c:forEach>
    </c:if>
    <c:if test="${empty orderList}">
        <div class="text-header">
            There are no orders
            <form action="${pageContext.servletContext.contextPath}/products/cart/order/overview" method="post" class="inline">
                <button name="update" type="submit" class="update-button"></button>
            </form>
        </div>
    </c:if>
</tags:master>
