package com.es.phoneshop.service;

import com.es.phoneshop.exception.ProductIdNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.RecentlyViewedProducts;

import javax.servlet.http.HttpServletRequest;

public interface RecentlyViewedProductsService {
    RecentlyViewedProducts getRecentlyViewedProducts(HttpServletRequest request);
    void addProduct(HttpServletRequest request, String productId)
            throws ProductNotFoundException, ProductIdNotFoundException;
}
