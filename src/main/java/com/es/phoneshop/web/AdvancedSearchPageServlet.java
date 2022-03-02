package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdvancedSearchPageServlet extends HttpServlet {

    public static final String ADVANCED_SEARCH_PAGE_JSP = "/WEB-INF/pages/advancedSearchPage.jsp";
    public static final String PRODUCT_CODE_PARAMETER = "productCode";
    public static final String MIN_PRICE_PARAMETER = "minPrice";
    public static final String MAX_PRICE_PARAMETER = "maxPrice";
    public static final String MIN_STOCK_PARAMETER = "minStock";
    public static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String ERROR_ATTRIBUTE = "error";
    public static final String ERROR_NOT_NUMBER_MESSAGE = "error.not.number.message";
    public static final String ERROR_NON_POSITIVE_NUMBER_MESSAGE = "error.non.positive.number.message";
    public static final String MIN_PRICE_ERROR_ATTRIBUTE = "minPriceError";
    public static final String MAX_PRICE_ERROR_ATTRIBUTE = "maxPriceError";
    public static final String MIN_STOCK_ERROR_ATTRIBUTE = "minStockError";

    private ProductDao productDao;
    private ResourceBundle errorMessages;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        errorMessages = ResourceBundle.getBundle(ERROR_ATTRIBUTE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Objects.isNull(request.getParameter("search"))){
            request.getRequestDispatcher(ADVANCED_SEARCH_PAGE_JSP).forward(request, response);
            return;
        }
        String stringProductCode = request.getParameter(PRODUCT_CODE_PARAMETER);
        String stringMinPrice = request.getParameter(MIN_PRICE_PARAMETER);
        String stringMaxPrice = request.getParameter(MAX_PRICE_PARAMETER);
        String stringMinStock = request.getParameter(MIN_STOCK_PARAMETER);

        NumberFormat format = NumberFormat.getInstance(request.getLocale());

        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;

        if (validatePrice(request, stringMinPrice, format, MIN_PRICE_ERROR_ATTRIBUTE)) {
            try {
                minPrice = BigDecimal.valueOf(format.parse(stringMinPrice).doubleValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (validatePrice(request, stringMaxPrice, format, MAX_PRICE_ERROR_ATTRIBUTE)) {
            try {
                maxPrice = BigDecimal.valueOf(format.parse(stringMaxPrice).doubleValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int minStock = 1;
        if (validateMinStock(request, stringMinStock)) {
            minStock = Integer.parseInt(stringMinStock);
        }

        if (Objects.isNull(request.getAttribute(MIN_PRICE_ERROR_ATTRIBUTE))
                && Objects.isNull(request.getAttribute(MAX_PRICE_ERROR_ATTRIBUTE))
                && Objects.isNull(request.getAttribute(MIN_STOCK_ERROR_ATTRIBUTE))) {
            request.setAttribute(PRODUCTS_ATTRIBUTE,
                    productDao.findProducts(stringProductCode, minPrice, maxPrice, minStock));
        }

        request.getRequestDispatcher(ADVANCED_SEARCH_PAGE_JSP).forward(request, response);
    }

    private boolean validatePrice(HttpServletRequest request, String stringPrice, NumberFormat format, String error) {
        BigDecimal price = null;
        if (!StringUtils.isBlank(stringPrice)) {
            if (!stringPrice.matches("(-?[1-9][0-9]*(,0+)?)")) {
                request.setAttribute(error, errorMessages.getString(ERROR_NOT_NUMBER_MESSAGE));
                return false;
            }
            try {
                price = BigDecimal.valueOf(format.parse(stringPrice).doubleValue());
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    request.setAttribute(error, errorMessages.getString(ERROR_NON_POSITIVE_NUMBER_MESSAGE));
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private boolean validateMinStock(HttpServletRequest request, String stringMinStock) {
        int minStock = 1;
        if (!StringUtils.isBlank(stringMinStock)) {
            if (!stringMinStock.matches("-?[0-9]+")) {
                request.setAttribute(MIN_STOCK_ERROR_ATTRIBUTE, errorMessages.getString(ERROR_NOT_NUMBER_MESSAGE));
                return false;
            }
            minStock = Integer.parseInt(stringMinStock);
            if (minStock <= 0) {
                request.setAttribute(MIN_STOCK_ERROR_ATTRIBUTE, errorMessages.getString(ERROR_NON_POSITIVE_NUMBER_MESSAGE));
                return false;
            }
            return true;
        }
        return false;
    }
}
