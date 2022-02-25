package com.es.phoneshop.dao;

import com.es.phoneshop.exception.ProductIdNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Comparator;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private static ProductDao instance;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    public static synchronized ProductDao getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    @Override
    public synchronized Product getProduct(String productId) throws ProductNotFoundException, ProductIdNotFoundException {
        if (StringUtils.isBlank(productId)) {
            throw new ProductIdNotFoundException();
        }
        return products.stream()
                .filter(product -> Objects.equals(productId, product.getId()))
                .findAny()
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public synchronized void deleteAllProducts() {
        products.clear();
    }

    @Override
    public synchronized List<Product> findProducts() {
        return products.stream()
                .filter(product -> product.getPrice() != null)
                .filter(this::productIsInStock)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = Comparator.comparing((p) -> false);
        if (SortField.description.equals(sortField)) {
            comparator = Comparator.comparing(Product::getDescription);
        } else if (SortField.price.equals(sortField)) {
            comparator = Comparator.comparing(Product::getPrice);
        }
        Stream<Product> stream = products.stream();
        if (!StringUtils.isBlank(query)) {
            String[] words = query.split(StringUtils.SPACE);
            comparator = comparator.thenComparing(Product::getDescription, (firstDescription, secondDescription) ->
                    Integer.compare(firstDescription.split(StringUtils.SPACE).length,
                            secondDescription.split(StringUtils.SPACE).length));
            stream = stream
                    .filter(product -> Arrays.stream(words).allMatch(word -> product.getDescription().contains(word)));
        }
        if (SortOrder.desc.equals(sortOrder)) {
            comparator = comparator.reversed();
        }
        return stream
                .filter(product -> Objects.nonNull(product.getPrice()))
                .filter(this::productIsInStock)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private boolean productIsInStock(Product product) {
        if (Objects.isNull(product)) {
            return false;
        }
        return product.getStock() > 0;
    }

    @Override
    public synchronized void save(Product product) {
        products.stream()
                .filter(element -> Objects.equals(product.getId(), element.getId()))
                .findAny()
                .ifPresentOrElse(element -> products.set(products.indexOf(element), product)
                        , () -> products.add(product));
    }

    @Override
    public synchronized void delete(String productId) throws ProductNotFoundException, ProductIdNotFoundException {
        if (StringUtils.isBlank(productId)) {
            throw new ProductIdNotFoundException();
        }
        int size = products.size();
        products = products.stream()
                .filter(x -> !productId.equals(x.getId()))
                .collect(Collectors.toList());
        if (size == products.size()) {
            throw new ProductNotFoundException();
        }
    }
}
