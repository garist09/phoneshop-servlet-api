package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class OrderOverviewPageServlet extends HttpServlet {
    public static final String ORDER_IS_PLACED_JSP = "/WEB-INF/pages/orderIsPlaced.jsp";
    public static final String DELETE_ORDER_ID_PARAMETER = "deleteOrderId";
    public static final String UPDATE_PARAMETER = "update";

    private OrderDao orderDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(ORDER_IS_PLACED_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter(DELETE_ORDER_ID_PARAMETER);
        if (Objects.nonNull(request.getParameter(UPDATE_PARAMETER))) {
            doGet(request, response);
            return;
        }
        orderDao.deleteOrder(request, orderId);
        doGet(request, response);
    }
}
