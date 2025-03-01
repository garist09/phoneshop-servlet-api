package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductIdNotFoundException;
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

public class HttpSessionRecentlyViewedProductsServiceImpl implements RecentlyViewedProductsService {
    private static final String RECENTLY_VIEWED = "recentlyViewed";
    private static final Object LOCK;

    private static HttpSessionRecentlyViewedProductsServiceImpl instance;

    private ProductDao productDao;

    static {
        LOCK = new Object();
    }

    private HttpSessionRecentlyViewedProductsServiceImpl() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static HttpSessionRecentlyViewedProductsServiceImpl getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (LOCK) {
                instance = new HttpSessionRecentlyViewedProductsServiceImpl();
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
    public void addProduct(HttpServletRequest request, String productId)
            throws ProductNotFoundException, ProductIdNotFoundException {
        if (StringUtils.isBlank(productId)) {
            throw new ProductIdNotFoundException();
        }
        List<Product> productList = getRecentlyViewedProducts(request).getProducts();
        Product product = productDao.getProduct(productId);

        if (!productList.contains(product)) {
            synchronized (request.getSession()) {
                if (productList.size() > NumberUtils.INTEGER_TWO) {
                    productList.remove(NumberUtils.INTEGER_ZERO.intValue());
                }
                productList.add(product);
            }
        }
    }
}
