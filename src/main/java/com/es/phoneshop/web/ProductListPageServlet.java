package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.product.IdNotFoundException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private static final String attribute = "products";
    private static final String path = "/WEB-INF/pages/productList.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("searchMobile");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        try {
            request.setAttribute(attribute, productDao.findProducts(query, Optional.ofNullable(sortField).map(field -> SortField.valueOf(field)).orElse(null), Optional.ofNullable(sortOrder).map(order -> SortOrder.valueOf(order)).orElse(null)));
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
        request.getRequestDispatcher(path).forward(request, response);
    }
}
