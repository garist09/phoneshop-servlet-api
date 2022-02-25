<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<html>
<head>
  <title>${pageTitle}</title>
  <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
  <meta charset="UTF-8">
</head>
<body class="product-list">
  <header>
    <a href="${pageContext.servletContext.contextPath}/products">
      <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
      PhoneShop
    </a>
    <jsp:include page="/products/cart/miniCart"/>
      <a href="${pageContext.servletContext.contextPath}/products/cart">
        <img src="${pageContext.servletContext.contextPath}/images/cart.png" class="cart" align="right"/>
      </a>
    <a href="${pageContext.servletContext.contextPath}/products/cart/order/overview">
      <img src="${pageContext.servletContext.contextPath}/images/checklist.png" class="order" align="right"/>
    </a>
  </header>
  <main>
    <jsp:doBody/>
  </main>
  <c:if test="${!pageTitle.equals('Product List')}">
    <h3>
      <a href="${pageContext.servletContext.contextPath}/products">Return to product list</a>
    </h3>
  </c:if>
  <c:if test="${not empty recentlyViewed}">
    <h2>
      Recently viewed
    </h2>
    <table>
      <tr>
        <c:forEach var="product" items="${recentlyViewed.products}">
          <td class="center">
              <img class="product-tile" src="${product.imageUrl}">
              <br>
              <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                  ${product.description}
              </a>
              <br>
              <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}">
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
              </a>
          </td>
        </c:forEach>
      </tr>
    </table>
  </c:if>
  <p>
    &copy;09.RG
  </p>
</body>
</html>