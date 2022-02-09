package com.es.phoneshop.service;

import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void addProduct(HttpServletRequest request, String productId, int quantity) throws IdNotFoundException, OutOfStockException, ProductNotFoundException, IllegalArgumentException;
}
