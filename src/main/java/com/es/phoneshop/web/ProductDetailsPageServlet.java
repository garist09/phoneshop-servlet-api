package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.impl.CartServiceImpl;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.RecentlyViewedProducts;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String path = "/WEB-INF/pages/product.jsp";
    public static final String PRODUCT = "product";
    public static final int BEGIN_INDEX = 1;
    public static final String QUANTITY = "quantity";
    public static final String PURCHASED_JSP = "/WEB-INF/pages/productIsPurchased.jsp";
    public static final String ERROR = "error";
    public static final String NOT_A_NUMBER = "Not a number";
    public static final String AVAILABLE = "Available: ";

    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo().substring(BEGIN_INDEX);
        request.setAttribute(PRODUCT, productDao.getProduct(id));
        request.getRequestDispatcher(path).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo().substring(BEGIN_INDEX);
        String stringQuantity = request.getParameter(QUANTITY);
        int quantity;
        try{
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(stringQuantity).intValue();
            cartService.addProduct(request, id, quantity);
            request.getRequestDispatcher(PURCHASED_JSP).forward(request, response);
            return;
        }
        catch (ParseException ex) {
            request.setAttribute(ERROR, NOT_A_NUMBER);
        }
        catch (OutOfStockException ex) {
            request.setAttribute(ERROR, AVAILABLE + ex.getStock());
        }
        catch (IllegalArgumentException ex) {
            request.setAttribute(ERROR, ex.getMessage());
        }
        doGet(request, response);
    }
}
