package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedProducts {
    private List<Product> products;

    public RecentlyViewedProducts() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }
}
