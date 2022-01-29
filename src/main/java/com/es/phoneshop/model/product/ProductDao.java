package com.es.phoneshop.model.product;

import java.util.List;

public interface ProductDao {
    Product getProduct(String id) throws ProductNotFoundException, IdNotFoundException;
    List<Product> findProducts();
    void save(Product product);
    void delete(String id) throws ProductNotFoundException, IdNotFoundException;
}
