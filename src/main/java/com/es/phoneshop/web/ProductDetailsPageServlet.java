package com.es.phoneshop.web;

import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartServiceImpl;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.service.impl.HttpSessionRecentlyViewedProductsServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT_JSP_PAGE = "/WEB-INF/pages/product.jsp";
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String NOT_A_NUMBER_MESSAGE = "error.invalid.number.message";
    private static final String AVAILABLE_MESSAGE = "error.out.of.stock.message";
    private static final String NON_POSITIVE_QUANTITY = "error.not.positive.message";
    private static final String SUCCESS_MESSAGE_ATTRIBUTE = "successMessage";
    private static final String SUCCESSFULLY_ADDED_TO_THE_CART = "The product has been successfully added to the cart";
    private static final int BEGIN_INDEX = 1;

    private ResourceBundle errorMessages;
    private ProductDao productDao;
    private CartService cartService;
    private HttpSessionRecentlyViewedProductsServiceImpl recentlyViewedProducts;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = HttpSessionCartServiceImpl.getInstance();
        recentlyViewedProducts = HttpSessionRecentlyViewedProductsServiceImpl.getInstance();
        errorMessages = ResourceBundle.getBundle(ERROR_ATTRIBUTE, Locale.ENGLISH);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo().substring(BEGIN_INDEX);
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(id));
        recentlyViewedProducts.addProduct(request, id);

        request.getRequestDispatcher(PRODUCT_JSP_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getPathInfo().substring(BEGIN_INDEX);
        String stringQuantity = request.getParameter(QUANTITY_PARAMETER);
        int quantity = 0;
        NumberFormat format = NumberFormat.getInstance(request.getLocale());

        try {
            quantity = format.parse(stringQuantity).intValue();
            cartService.addProduct(request, id, quantity);
            request.setAttribute(SUCCESS_MESSAGE_ATTRIBUTE, SUCCESSFULLY_ADDED_TO_THE_CART);
        } catch (ParseException ex) {
            request.setAttribute(ERROR_ATTRIBUTE, errorMessages.getString(NOT_A_NUMBER_MESSAGE));
        } catch (OutOfStockException ex) {
            request.setAttribute(ERROR_ATTRIBUTE, errorMessages.getString(AVAILABLE_MESSAGE));
        } catch (IllegalArgumentException ex) {
            request.setAttribute(ERROR_ATTRIBUTE, errorMessages.getString(NON_POSITIVE_QUANTITY));
        }

        doGet(request, response);
    }
}
