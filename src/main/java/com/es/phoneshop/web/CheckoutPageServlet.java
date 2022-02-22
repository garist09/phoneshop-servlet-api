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

public class CheckoutPageServlet extends HttpServlet {

    public static final String ORDER_ATTRIBUTE = "order";
    public static final String PHONE_NUMBER_FIELD = "phone number";
    private static final String CHECKOUT_PAGE_JSP = "/WEB-INF/pages/checkoutPage.jsp";
    private static final String CART_ITEM_LIST_ATTRIBUTE = "cartItemList";
    private static final String TOTAL_PRICE_ATTRIBUTE = "totalPrice";
    private static final String DELIVERY_COST_ATTRIBUTE = "deliveryCost";
    private static final String DELIVERY_DATES_ATTRIBUTE = "deliveryDates";
    public static final String ERRORS_ATTRIBUTE = "errors";
    public static final String FIRST_NAME_FIELD = "first name";
    public static final String LAST_NAME_FIELD = "last name";
    public static final String DELIVERY_ADDRESS_FIELD = "delivery address";
    public static final String FIRST_NAME_PARAMETER = "firstName";
    public static final String LAST_NAME_PARAMETER = "lastName";
    public static final String PHONE_NUMBER_PARAMETER = "phoneNumber";
    public static final String DELIVERY_ADDRESS_PARAMETER = "deliveryAddress";
    public static final String DELIVERY_DATE_PARAMETER = "deliveryDate";
    public static final String PAYMENT_METHOD_PARAMETER = "paymentMethod";
    public static final String ORDER_OVERVIEW_SERVLET_PATH = "/products/cart/order/overview";
    public static final String ORDER_LIST_ATTRIBUTE = "orderList";
    public static final String FORMAT_DATE = "E dd.MM.yyyy";

    private static String[] deliveryDates;

    private CartService cartService;
    private OrderService orderService;
    private ProductDao productDao;
    private OrderDao orderDao;
    private Order order;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SimpleDateFormat formatDateNow = new SimpleDateFormat(FORMAT_DATE);
        Date date = new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(3));
        String firstDeliveryDate = formatDateNow.format(date);
        date.setTime(date.getTime() + TimeUnit.DAYS.toMillis(1));
        String secondDeliveryDate = formatDateNow.format(date);
        date.setTime(date.getTime() + TimeUnit.DAYS.toMillis(1));
        String thirdDeliveryDate = formatDateNow.format(date);
        deliveryDates = new String[]{firstDeliveryDate, secondDeliveryDate, thirdDeliveryDate};
        cartService = HttpSessionCartServiceImpl.getInstance();
        orderService = OrderServiceImpl.getInstance();
        productDao = ArrayListProductDao.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        order = orderService.createOrderFromCart(cart);
        request.setAttribute(ORDER_ATTRIBUTE, order);
        request.setAttribute(CART_ITEM_LIST_ATTRIBUTE, cart.getCartItems());
        request.setAttribute(TOTAL_PRICE_ATTRIBUTE, cart.getTotalPrice());
        request.setAttribute(DELIVERY_COST_ATTRIBUTE, orderService.createOrderFromCart(cart).getDeliveryCost());
        request.setAttribute(DELIVERY_DATES_ATTRIBUTE, deliveryDates);
        request.getRequestDispatcher(CHECKOUT_PAGE_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        order = orderService.createOrderFromCart(cart);
        List<String> errors = new ArrayList<>();
        setErrors(request, errors);
        request.setAttribute(ERRORS_ATTRIBUTE, errors);
        if (!errors.isEmpty()) {
            doGet(request, response);
            return;
        }
        List<CartItem> cartItemList = order.getCartItemList();
        for (CartItem cartItem : cartItemList) {
            Product product = productDao.getProduct(cartItem.getProduct().getId());
            product.setStock(product.getStock() - cartItem.getQuantity());
        }
        order.setFirstName(getParameterWithUTF8Encoding(request,FIRST_NAME_PARAMETER))
                .setLastName(getParameterWithUTF8Encoding(request, LAST_NAME_PARAMETER))
                .setPhoneNumber(getParameterWithUTF8Encoding(request, PHONE_NUMBER_PARAMETER))
                .setDeliveryAddress(getParameterWithUTF8Encoding(request, DELIVERY_ADDRESS_PARAMETER))
                .setDeliveryDate(getParameterWithUTF8Encoding(request, DELIVERY_DATE_PARAMETER))
                .setPaymentMethod(request.getParameter(PAYMENT_METHOD_PARAMETER));
        orderDao.addOrder(request, order);
        cartService.clearCart(request);
        request.getSession().setAttribute(ORDER_LIST_ATTRIBUTE, orderDao.getOrderList(request));
        response.sendRedirect(request.getContextPath() + ORDER_OVERVIEW_SERVLET_PATH);
    }

    private void setErrors(HttpServletRequest request, List<String> errors) {
        if (StringUtils.isBlank(request.getParameter(FIRST_NAME_PARAMETER))) {
            errors.add(FIRST_NAME_FIELD);
        }
        if (StringUtils.isBlank(request.getParameter(LAST_NAME_PARAMETER))) {
            errors.add(LAST_NAME_FIELD);
        }
        if (StringUtils.isBlank(request.getParameter(PHONE_NUMBER_PARAMETER))) {
            errors.add(PHONE_NUMBER_FIELD);
        }
        if (StringUtils.isBlank(request.getParameter(DELIVERY_ADDRESS_PARAMETER))) {
            errors.add(DELIVERY_ADDRESS_FIELD);
        }
    }

    private String getParameterWithUTF8Encoding(HttpServletRequest request, String parameter) {
        return new String (request.getParameter(parameter).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
