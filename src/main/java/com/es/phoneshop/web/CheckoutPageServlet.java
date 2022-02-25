package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.HttpSessionCartServiceImpl;
import com.es.phoneshop.service.impl.OrderServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckoutPageServlet extends HttpServlet {
    private static final String PHONE_NUMBER_FIELD = "phone number";
    private static final String CHECKOUT_PAGE_JSP = "/WEB-INF/pages/checkoutPage.jsp";
    private static final String CART_ITEM_LIST_ATTRIBUTE = "cartItemList";
    private static final String TOTAL_PRICE_ATTRIBUTE = "totalPrice";
    private static final String DELIVERY_COST_ATTRIBUTE = "deliveryCost";
    private static final String DELIVERY_DATES_ATTRIBUTE = "deliveryDates";
    private static final String FIRST_NAME_FIELD = "first name";
    private static final String LAST_NAME_FIELD = "last name";
    private static final String DELIVERY_ADDRESS_FIELD = "delivery address";
    private static final String FIRST_NAME_PARAMETER = "firstName";
    private static final String LAST_NAME_PARAMETER = "lastName";
    private static final String PHONE_NUMBER_PARAMETER = "phoneNumber";
    private static final String DELIVERY_ADDRESS_PARAMETER = "deliveryAddress";
    private static final String DELIVERY_DATE_PARAMETER = "deliveryDate";
    private static final String PAYMENT_METHOD_PARAMETER = "paymentMethod";
    private static final String ORDER_OVERVIEW_SERVLET_PATH = "/products/cart/order/overview";
    private static final String ORDER_LIST_ATTRIBUTE = "orderList";
    private static final String FORMAT_DATE = "E dd.MM.yyyy";
    private static final int START_DAY_COUNT = 3;
    private static final int TIME_UNTIL_NEXT_DATE = 1;
    private static final String EMPTY_FIELD_ERRORS_ATTRIBUTE = "emptyFieldErrors";
    private static final String INCORRECT_FIELD_ERRORS_ATTRIBUTE = "incorrectFieldErrors";
    private static final String ENTERED_FIRST_NAME_ATTRIBUTE = "enteredFirstName";
    private static final String ENTERED_LAST_NAME_ATTRIBUTE = "enteredLastName";
    private static final String ENTERED_PHONE_NUMBER_ATTRIBUTE = "enteredPhoneNumber";
    private static final String ENTERED_DELIVERY_ADDRESS_ATTRIBUTE = "enteredDeliveryAddress";

    private static String[] deliveryDates;

    private CartService cartService;
    private OrderService orderService;
    private ProductDao productDao;
    private OrderDao orderDao;
    private Order order;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initializeDeliveryDates();
        cartService = HttpSessionCartServiceImpl.getInstance();
        orderService = OrderServiceImpl.getInstance();
        productDao = ArrayListProductDao.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    private void initializeDeliveryDates() {
        SimpleDateFormat formatDateNow = new SimpleDateFormat(FORMAT_DATE);
        Date date = new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(START_DAY_COUNT));
        String firstDeliveryDate = formatDateNow.format(date);
        date.setTime(date.getTime() + TimeUnit.DAYS.toMillis(TIME_UNTIL_NEXT_DATE));
        String secondDeliveryDate = formatDateNow.format(date);
        date.setTime(date.getTime() + TimeUnit.DAYS.toMillis(TIME_UNTIL_NEXT_DATE));
        String thirdDeliveryDate = formatDateNow.format(date);
        deliveryDates = new String[]{firstDeliveryDate, secondDeliveryDate, thirdDeliveryDate};
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAllAttributes(request);

        request.getRequestDispatcher(CHECKOUT_PAGE_JSP).forward(request, response);
    }

    private void setAllAttributes(HttpServletRequest request) {
        Cart cart = cartService.getCart(request);
        request.setAttribute(CART_ITEM_LIST_ATTRIBUTE, cart.getCartItems());
        request.setAttribute(TOTAL_PRICE_ATTRIBUTE, cart.getTotalPrice());
        request.setAttribute(DELIVERY_COST_ATTRIBUTE, orderService.createOrderFromCart(cart).getDeliveryCost());
        request.setAttribute(DELIVERY_DATES_ATTRIBUTE, deliveryDates);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        order = orderService.createOrderFromCart(cart);
        List<String> emptyFieldErrors = new ArrayList<>();
        List<String> incorrectFieldErrors = new ArrayList<>();

        checkEmptyParameters(request, emptyFieldErrors);
        checkForCorrectParameters(request, incorrectFieldErrors);

        setAllFieldsAttributes(request);

        if (!emptyFieldErrors.isEmpty() || !incorrectFieldErrors.isEmpty()) {
            request.setAttribute(EMPTY_FIELD_ERRORS_ATTRIBUTE, emptyFieldErrors);
            request.setAttribute(INCORRECT_FIELD_ERRORS_ATTRIBUTE, incorrectFieldErrors);
            doGet(request, response);
            return;
        }

        List<CartItem> cartItemList = order.getCartItemList();
        cartItemList.forEach(cartItem -> {
            Product product = productDao.getProduct(cartItem.getProduct().getId());
            product.setStock(product.getStock() - cartItem.getQuantity());
        });
        setAllFields(request);
        orderDao.addOrder(request, order);
        cartService.clearCart(request);
        request.getSession().setAttribute(ORDER_LIST_ATTRIBUTE, orderDao.getOrderList(request));

        response.sendRedirect(request.getContextPath() + ORDER_OVERVIEW_SERVLET_PATH);
    }

    private void setAllFields(HttpServletRequest request) {
        order.setFirstName(getParameterWithUTF8Encoding(request, FIRST_NAME_PARAMETER));
        order.setLastName(getParameterWithUTF8Encoding(request, LAST_NAME_PARAMETER));
        order.setPhoneNumber(getParameterWithUTF8Encoding(request, PHONE_NUMBER_PARAMETER));
        order.setDeliveryAddress(getParameterWithUTF8Encoding(request, DELIVERY_ADDRESS_PARAMETER));
        order.setDeliveryDate(getParameterWithUTF8Encoding(request, DELIVERY_DATE_PARAMETER));
        order.setPaymentMethod(request.getParameter(PAYMENT_METHOD_PARAMETER));
    }

    private void setAllFieldsAttributes(HttpServletRequest request) {
        if (!StringUtils.isBlank(request.getParameter(FIRST_NAME_PARAMETER))) {
            request.setAttribute(ENTERED_FIRST_NAME_ATTRIBUTE,
                    getParameterWithUTF8Encoding(request, FIRST_NAME_PARAMETER));
        } else {
            request.setAttribute(ENTERED_FIRST_NAME_ATTRIBUTE, StringUtils.EMPTY);
        }

        if (!StringUtils.isBlank(request.getParameter(LAST_NAME_PARAMETER))) {
            request.setAttribute(ENTERED_LAST_NAME_ATTRIBUTE,
                    getParameterWithUTF8Encoding(request, LAST_NAME_PARAMETER));
        } else {
            request.setAttribute(ENTERED_LAST_NAME_ATTRIBUTE, StringUtils.EMPTY);
        }

        if (!StringUtils.isBlank(request.getParameter(PHONE_NUMBER_PARAMETER))) {
            request.setAttribute(ENTERED_PHONE_NUMBER_ATTRIBUTE,
                    getParameterWithUTF8Encoding(request, PHONE_NUMBER_PARAMETER));
        } else {
            request.setAttribute(ENTERED_PHONE_NUMBER_ATTRIBUTE, StringUtils.EMPTY);
        }

        if (!StringUtils.isBlank(request.getParameter(DELIVERY_ADDRESS_PARAMETER))) {
            request.setAttribute(ENTERED_DELIVERY_ADDRESS_ATTRIBUTE,
                    getParameterWithUTF8Encoding(request, DELIVERY_ADDRESS_PARAMETER));
        } else {
            request.setAttribute(ENTERED_DELIVERY_ADDRESS_ATTRIBUTE, StringUtils.EMPTY);
        }
    }

    private void checkEmptyParameters(HttpServletRequest request, List<String> emptyFieldErrors) {
        if (StringUtils.isBlank(request.getParameter(FIRST_NAME_PARAMETER))) {
            emptyFieldErrors.add(FIRST_NAME_FIELD);
        }
        if (StringUtils.isBlank(request.getParameter(LAST_NAME_PARAMETER))) {
            emptyFieldErrors.add(LAST_NAME_FIELD);
        }
        if (StringUtils.isBlank(request.getParameter(PHONE_NUMBER_PARAMETER))) {
            emptyFieldErrors.add(PHONE_NUMBER_FIELD);
        }
        if (StringUtils.isBlank(request.getParameter(DELIVERY_ADDRESS_PARAMETER))) {
            emptyFieldErrors.add(DELIVERY_ADDRESS_FIELD);
        }
    }

    private void checkForCorrectParameters(HttpServletRequest request, List<String> incorrectFieldErrors) {
        Pattern namePattern = Pattern.compile("^[\\p{IsAlphabetic}\\-]+$");
        Matcher firstNameMatcher = namePattern.matcher(getParameterWithUTF8Encoding(request, FIRST_NAME_PARAMETER));
        Matcher lastNameMatcher = namePattern.matcher(getParameterWithUTF8Encoding(request, LAST_NAME_PARAMETER));
        Pattern phonePattern = Pattern.compile("^(80|(\\+?375))(33|29|44)\\d{7}$");
        Matcher phoneNumberMatcher = phonePattern.matcher(request.getParameter(PHONE_NUMBER_PARAMETER));

        if (!firstNameMatcher.matches()) {
            incorrectFieldErrors.add(FIRST_NAME_FIELD);
        }
        if (!lastNameMatcher.matches()) {
            incorrectFieldErrors.add(LAST_NAME_FIELD);
        }
        if (!phoneNumberMatcher.matches()) {
            incorrectFieldErrors.add(PHONE_NUMBER_FIELD);
        }
    }

    private String getParameterWithUTF8Encoding(HttpServletRequest request, String parameter) {
        return new String(request.getParameter(parameter).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
