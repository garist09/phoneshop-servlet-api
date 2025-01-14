package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductListPageServlet extends HttpServlet {
    private static final String SORT = "sort";
    private static final String ID_PARAMETER = "id";
    private static final String attribute = "products";
    private static final String path = "/WEB-INF/pages/productList.jsp";
    private static final String SEARCH_MOBILE = "searchMobile";
    private static final String ORDER = "order";
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String NOT_A_NUMBER_MESSAGE = "error.invalid.number.message";
    private static final String AVAILABLE_MESSAGE = "error.out.of.stock.message";
    private static final String NON_POSITIVE_QUANTITY = "error.not.positive.message";
    private static final String SUCCESS_MESSAGE_ATTRIBUTE = "successMessage";
    private static final String SUCCESSFULLY_ADDED_TO_THE_CART = "The product has been successfully added to the cart";
    private static final String PARSE_EXCEPTION_MESSAGE = "The string contains invalid characters";
    private static final int CHARACTER_OUTSIDE_STRING = -1;

    private ProductDao productDao;
    private CartService cartService;
    private ResourceBundle errorMessages;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = HttpSessionCartServiceImpl.getInstance();
        errorMessages = ResourceBundle.getBundle(ERROR_ATTRIBUTE);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter(ID_PARAMETER);
        String stringQuantity = request.getParameter(QUANTITY_PARAMETER);
        int quantity = 0;
        NumberFormat format = NumberFormat.getInstance(request.getLocale());

        try {
            if (!stringQuantity.matches("([1-9][0-9]*(,0+)?)")) {
                throw new ParseException(PARSE_EXCEPTION_MESSAGE, CHARACTER_OUTSIDE_STRING);
            }
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