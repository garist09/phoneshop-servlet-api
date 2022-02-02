package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private ProductDao productDao;
    private static final String path = "/WEB-INF/pages/productPrice.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo().substring(1);
        request.setAttribute("product", productDao.getProduct(id));
        request.setAttribute("currentDate", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        request.getRequestDispatcher(path).forward(request, response);
    }
}
