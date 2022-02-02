<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>

<a href="?sort=${sort}&order=${order}&searchMobile=${param.searchMobile}">
    ${(sort eq param.sort and order eq param.order) ? (order eq "asc" ? '&#129093;' : '&#129095;') : (order eq "asc" ? '&#8679;' : '&#8681;')}</a>