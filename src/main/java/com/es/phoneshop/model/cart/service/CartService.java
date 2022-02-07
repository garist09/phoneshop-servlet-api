package com.es.phoneshop.model.cart.service;

import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void addProduct(HttpServletRequest request, String id, int quantity) throws IdNotFoundException, OutOfStockException, ProductNotFoundException, IllegalArgumentException;
}
