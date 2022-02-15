package com.es.phoneshop.web;

import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

public class CartItemDeleteServlet extends HttpServlet {
    public static final String DELETE_PARAMETER = "delete";
    public static final String CART_SERVLET = "/products/cart";

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (Objects.nonNull(request.getParameter(DELETE_PARAMETER))) {
            cartService.deleteProduct(request, request.getParameter(DELETE_PARAMETER));
        }
        response.sendRedirect(request.getContextPath() + CART_SERVLET);
    }
}
