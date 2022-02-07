package com.es.phoneshop.model.cart.service.impl;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class CartServiceImpl implements CartService {
    public static final String CART_ATTRIBUTE = "CartServiceImpl.CART";
    private static CartService instance;

    private ProductDao productDao;

    private CartServiceImpl() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static CartService getInstance() {
        if (Objects.isNull(instance)) {
            instance = new CartServiceImpl();
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart;
        if (Objects.isNull(session.getAttribute(CART_ATTRIBUTE))) {
            cart = new Cart();
            session.setAttribute(CART_ATTRIBUTE, cart);
        } else {
            cart = (Cart) session.getAttribute(CART_ATTRIBUTE);
        }
        return cart;
    }

    @Override
    public void addProduct(HttpServletRequest request, String id, int quantity) throws IdNotFoundException, OutOfStockException, ProductNotFoundException, IllegalArgumentException {
        if (Objects.isNull(id)) {
            throw new IdNotFoundException();
        }
        Product product = productDao.getProduct(id);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product.getStock());
        }
        getCart(request).add(product, quantity);
    }
}
