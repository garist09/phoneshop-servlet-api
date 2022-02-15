package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.HttpSessionCartService;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class HttpSessionCartServlet extends HttpServlet {
    public static final String CART_JSP = "/WEB-INF/pages/cart.jsp";
    public static final String CART_LIST = "cartList";
    public static final String TOTAL_PRICE = "totalPrice";
    public static final String REMOVE_PARAMETER = "remove";
    public static final String ADD_PARAMETER = "add";

    private CartService cartService;
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = HttpSessionCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute(CART_LIST, cart.getProducts());
        request.setAttribute(TOTAL_PRICE, cart.getTotalPrice());
        request.getRequestDispatcher(CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Objects.nonNull(request.getParameter(REMOVE_PARAMETER))) {
            cartService.removeProduct(request, request.getParameter(REMOVE_PARAMETER), NumberUtils.INTEGER_ONE);
        }
        if (Objects.nonNull(request.getParameter(ADD_PARAMETER))) {
            Product product = productDao.getProduct(request.getParameter(ADD_PARAMETER));
            Optional<CartItem> optionalCartItem = cartService.getCart(request).getProducts().stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product))
                    .findAny();
            if (optionalCartItem.isPresent()) {
                CartItem cartItem = optionalCartItem.get();
                if (cartItem.getQuantity() < product.getStock()) {
                    cartService.addProduct(request, request.getParameter(ADD_PARAMETER), NumberUtils.INTEGER_ONE);
                }
            }
        }
        doGet(request, response);
    }
}
