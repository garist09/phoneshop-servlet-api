package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecentlyViewedProducts {
    public static final int INT2 = 2;
    public static final int INT0 = 0;
    public static final String RECENTLY_VIEWED = "recentlyViewed";

    private static RecentlyViewedProducts instance;

    private ProductDao productDao;

    private RecentlyViewedProducts() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static RecentlyViewedProducts getInstance(){
        if(Objects.isNull(instance)){
            instance = new RecentlyViewedProducts();
        }
        return instance;
    }

    public List<Product> getProductList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Product> productList;
        if (Objects.isNull(session.getAttribute(RECENTLY_VIEWED))) {
            productList = new ArrayList<>();
            session.setAttribute(RECENTLY_VIEWED, productList);
        } else {
            productList = (List<Product>) session.getAttribute(RECENTLY_VIEWED);
        }
        return productList;
    }

    public void addProduct(HttpServletRequest request, String id) throws ProductNotFoundException, IdNotFoundException {
        if (Objects.isNull(id)) {
            throw new IdNotFoundException();
        }
        List<Product> productList = getProductList(request);
        Product product = productDao.getProduct(id);
        if (!productList.contains(product)) {
            if (productList.size() > INT2) {
                productList.remove(INT0);
            }
            productList.add(product);
        }
    }
}
