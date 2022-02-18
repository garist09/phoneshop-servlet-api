package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.exception.QuantityOutOfBoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.model.product.Product;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HttpSessionCartServiceImpl implements CartService {
    public static final String CART_ATTRIBUTE = "sessionCart";
    private static final Object lock;

    private static CartService instance;

    private ProductDao productDao;

    static {
        lock = new Object();
    }

    private HttpSessionCartServiceImpl() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static CartService getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (lock) {
                instance = new HttpSessionCartServiceImpl();
            }
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart;
        if (Objects.isNull(session.getAttribute(CART_ATTRIBUTE))) {
            synchronized (session) {
                cart = new Cart();
            }
            session.setAttribute(CART_ATTRIBUTE, cart);
        } else {
            cart = (Cart) session.getAttribute(CART_ATTRIBUTE);
        }
        return cart;
    }

    @Override
    public void addProduct(HttpServletRequest request, String productId, int quantity)
            throws IdNotFoundException, OutOfStockException, ProductNotFoundException, IllegalArgumentException {
        checkingProductIdAndQuantity(productId, quantity);
        Product product = productDao.getProduct(productId);
        synchronized (request.getSession()) {
            List<CartItem> cartItems = getCart(request).getCartItems();
            Optional<CartItem> optionalCartItem = cartItems.stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product))
                    .findAny();
            if (optionalCartItem.isPresent()) {
                CartItem cartItem = optionalCartItem.get();
                if (product.getStock() < (quantity + cartItem.getQuantity())) {
                    throw new OutOfStockException(product.getStock());
                }
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                if (product.getStock() < quantity) {
                    throw new OutOfStockException(product.getStock());
                }
                cartItems.add(new CartItem(product, quantity));
            }
        }
        recalculateCart(request);
    }

    @Override
    public void deleteProduct(HttpServletRequest request, String productId) throws IdNotFoundException {
        if (StringUtils.isBlank(productId)) {
            throw new IdNotFoundException();
        }
        Product product = productDao.getProduct(productId);
        synchronized (request.getSession()) {
            List<CartItem> cartItems = getCart(request).getCartItems();
            cartItems.removeIf(cartItem -> cartItem.getProduct().equals(product));
        }
        recalculateCart(request);
    }

    private void checkingProductIdAndQuantity(String productId, int quantity)
            throws IdNotFoundException, IllegalArgumentException, OutOfStockException {
        if (StringUtils.isBlank(productId)) {
            throw new IdNotFoundException();
        }
        if (quantity <= NumberUtils.INTEGER_ZERO) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void removeProduct(HttpServletRequest request, String productId, int quantity)
            throws IdNotFoundException, IllegalArgumentException, OutOfStockException {
        checkingProductIdAndQuantity(productId, quantity);
        Product product = productDao.getProduct(productId);
        synchronized (request.getSession()) {
            List<CartItem> cartItems = getCart(request).getCartItems();
            Optional<CartItem> optionalCartItem = cartItems.stream()
                    .filter(cartItem -> cartItem.getProduct().equals(product))
                    .findAny();
            if (optionalCartItem.isPresent()) {
                CartItem cartItem = optionalCartItem.get();
                if (cartItem.getQuantity() < quantity) {
                    throw new QuantityOutOfBoundException();
                }
                cartItem.setQuantity(cartItem.getQuantity() - quantity);
            } else {
                throw new ProductNotFoundException();
            }
            cartItems.removeIf(this::quantityComparing);
        }
        recalculateCart(request);
    }

    private boolean quantityComparing(CartItem cartItem) {
        if (Objects.isNull(cartItem)) {
            return true;
        }
        return cartItem.getQuantity() <= 0;
    }

    private void recalculateCart(HttpServletRequest request) {
        Cart cart = getCart(request);
        List<CartItem> cartItems = cart.getCartItems();
        cart.setTotalPrice(cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        cart.setTotalQuantity(cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum());
    }
}