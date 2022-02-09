package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.RecentlyViewedProducts;
import com.es.phoneshop.service.RecentlyViewedProductsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

public class HttpSessionRecentlyViewedProductsService implements RecentlyViewedProductsService {
    public static final String RECENTLY_VIEWED = "recentlyViewed";

    private static HttpSessionRecentlyViewedProductsService instance;
    private static final Object lock;

    private ProductDao productDao;

    static {
        lock = new Object();
    }

    private HttpSessionRecentlyViewedProductsService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static HttpSessionRecentlyViewedProductsService getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (lock) {
                instance = new HttpSessionRecentlyViewedProductsService();
            }
        }
        return instance;
    }

    @Override
    public RecentlyViewedProducts getRecentlyViewedProducts(HttpServletRequest request) {
        HttpSession session = request.getSession();
        RecentlyViewedProducts recentlyViewedProducts;
        if (Objects.isNull(session.getAttribute(RECENTLY_VIEWED))) {
            synchronized (session) {
                recentlyViewedProducts = new RecentlyViewedProducts();
            }
            session.setAttribute(RECENTLY_VIEWED, recentlyViewedProducts);
        } else {
            recentlyViewedProducts = (RecentlyViewedProducts) session.getAttribute(RECENTLY_VIEWED);
        }
        return recentlyViewedProducts;
    }

    @Override
    public void addProduct(HttpServletRequest request, String productId) throws ProductNotFoundException, IdNotFoundException {
        if (StringUtils.isBlank(productId)) {
            throw new IdNotFoundException();
        }
        List<Product> productList = getRecentlyViewedProducts(request).getProducts();
        Product product = productDao.getProduct(productId);
        if (!productList.contains(product)) {
            synchronized (request) {
                if (productList.size() > NumberUtils.INTEGER_TWO) {
                    productList.remove(NumberUtils.INTEGER_ZERO.intValue());
                }
                productList.add(product);
            }
        }
    }
}
