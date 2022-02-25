<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cartItemList" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="totalPrice" type="java.math.BigDecimal" scope="request"/>
<jsp:useBean id="deliveryCost" type="java.math.BigDecimal" scope="request"/>
<jsp:useBean id="deliveryDates" type="java.lang.String[]" scope="request"/>
<tags:master pageTitle="Checkout page" >
    <c:if test="${not empty cartItemList}">
        <div class="text-header">
            Making an order
        </div>
        <br>
        <c:if test = "${not empty emptyFieldErrors}">
            <c:forEach var="errorField" items="${emptyFieldErrors}">
                <p class="red">
                    The ${errorField} field is not filled in
                </p>
            </c:forEach>
        </c:if>
        <c:if test = "${not empty incorrectFieldErrors}">
            <c:forEach var="errorField" items="${incorrectFieldErrors}">
                <p class="red">
                    The ${errorField} field is incorrect
                </p>
            </c:forEach>
        </c:if>
        <form method="post">
            <table>
                <tr>
                    <td>
                        First name
                    </td>
                    <td>
                        <input required placeholder="First name" type="text" name="firstName" value="${enteredFirstName}">
                        <span class="required">*</span>
                    </td>
                </tr>
                <tr>
                    <td>
                        Last name
                    </td>
                    <td>
                        <div>
                            <input required placeholder="Last name" type="text" name="lastName" value="${enteredLastName}">
                            <span class="required">*</span>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        Phone number
                    </td>
                    <td>
                        <input required placeholder="Phone number" name="phoneNumber" type="tel" value="${enteredPhoneNumber}">
                        <span class="required">*</span>
                    </td>
                </tr>
                <tr>
                    <td>
                        Delivery address
                    </td>
                    <td>
                        <input required placeholder="Delivery address" type="text" name="deliveryAddress" value="${enteredDeliveryAddress}">
                        <span class="required">*</span>
                    </td>
                </tr>
                <tr>
                    <td>
                        Delivery date
                    </td>
                    <td>
                        <select name="deliveryDate">
                            <option value="${deliveryDates[0]}">${deliveryDates[0]}</option>
                            <option value="${deliveryDates[1]}">${deliveryDates[1]}</option>
                            <option value="${deliveryDates[2]}">${deliveryDates[2]}</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        Payment method
                    </td>
                    <td>
                        <select name="paymentMethod">
                            <option value="cash">cash</option>
                            <option value="creditCard">credit card</option>
                        </select>
                    </td>
                </tr>
            </table>
            <p>
                <button>Place an order</button>
            </p>
        </form>
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
            <c:forEach var="cartItem" items="${cartItemList}">
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
        <p class="text-body-left">
            Subtotal: <fmt:formatNumber value="${totalPrice.doubleValue()}" type="currency"
                                           currencySymbol="${cartItemList.get(0).product.currency.symbol}"/>
        </p>
        <p class="text-body-left">
            Delivery cost: <fmt:formatNumber value="${deliveryCost.doubleValue()}" type="currency"
                                              currencySymbol="${cartItemList.get(0).product.currency.symbol}"/>
        </p>
        <p class="text-body-left">
            Cart total: <fmt:formatNumber value="${deliveryCost.doubleValue() + totalPrice.doubleValue()}" type="currency"
                                          currencySymbol="${cartItemList.get(0).product.currency.symbol}"/>
        </p>
    </c:if>
</tags:master>