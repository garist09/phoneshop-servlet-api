package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    public static final String SORT = "sort";
    private static final String attribute = "products";
    private static final String path = "/WEB-INF/pages/productList.jsp";
    public static final String SEARCH_MOBILE = "searchMobile";
    public static final String ORDER = "order";

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter(SEARCH_MOBILE);
        String sortField = request.getParameter(SORT);
        String sortOrder = request.getParameter(ORDER);
        try {
            request.setAttribute(attribute, productDao.findProducts(query,
                    Optional.ofNullable(sortField).map(field -> SortField.valueOf(field)).orElse(null),
                    Optional.ofNullable(sortOrder).map(order -> SortOrder.valueOf(order)).orElse(null)));
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
        request.getRequestDispatcher(path).forward(request, response);
    }
}
