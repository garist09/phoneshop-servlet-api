<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <br>
  <form>
    <input name="searchMobile" value="${param.searchMobile}">
    <button>Search</button>
  </form>
  <c:if test="${not empty error}">
    <p class="red">
        ${error}
    </p>
  </c:if>
  <table>
    <thead>
    <tr>
      <td>Image</td>
      <td>
          Description
          <tags:sortLink sort="description" order="asc"/>
          <tags:sortLink sort="description" order="desc"/>
      </td>
      <td>
        Quantity
      </td>
      <td class="price">
          Price
          <tags:sortLink sort="price" order="asc"/>
          <tags:sortLink sort="price" order="desc"/>
      </td>
    </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}" alt="Phone image">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}
          </a>
        </td>
        <td>
          <input name="quantity" value="1" form="${product.id}">
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
        </td>
        <td>
          <form name="${product.id}" method="post" id="${product.id}">
            <button name="id" type="submit" value="${product.id}">
              Add to Cart
            </button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>