package com.es.phoneshop.dao;

import com.es.phoneshop.exception.ProductIdNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;

import java.util.List;

public interface ProductDao {
    Product getProduct(String id) throws ProductNotFoundException, ProductIdNotFoundException;
    void deleteAllProducts();
    List<Product> findProducts();
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    void save(Product product);
    void delete(String id) throws ProductNotFoundException, ProductIdNotFoundException;
}
