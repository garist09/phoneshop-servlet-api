package com.es.phoneshop.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;

public class ErrorProductNotFoundServlet extends HttpServlet {
    private static final String path = "/WEB-INF/pages/errorProductNotFound.jsp";
    private static final String MESSAGE = "message";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Exception attribute = (Exception) request.getAttribute(ERROR_EXCEPTION);
        request.setAttribute(MESSAGE, attribute.getClass().toString());

        request.getRequestDispatcher(path).forward(request, response);
    }
}
