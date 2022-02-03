package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ArrayListProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String path = "/WEB-INF/pages/product.jsp";
    public static final String PRODUCT = "product";
    public static final int BEGIN_INDEX = 1;

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo().substring(BEGIN_INDEX);
        request.setAttribute(PRODUCT, productDao.getProduct(id));
        request.getRequestDispatcher(path).forward(request, response);
    }
}
