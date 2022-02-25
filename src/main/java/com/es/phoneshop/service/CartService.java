package com.es.phoneshop.service;

import com.es.phoneshop.exception.ProductIdNotFoundException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void addProduct(HttpServletRequest request, String productId, int quantity)
            throws ProductIdNotFoundException, OutOfStockException, ProductNotFoundException, IllegalArgumentException;
    void deleteProduct(HttpServletRequest request, String productId);
    void removeProduct(HttpServletRequest request, String productId, int quantity);
    void clearCart(HttpServletRequest request);
}
