package com.es.phoneshop.model.product;

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
        if (instance == null)
            instance = new ArrayListProductDao();
        return instance;
    }

    @Override
    public synchronized Product getProduct(String id) throws ProductNotFoundException, IdNotFoundException {
        if (id == null)
            throw new IdNotFoundException();
        return products.stream()
                .filter(product -> Objects.equals(id, product.getId()))
                .findAny()
                .orElseThrow(ProductNotFoundException::new);
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
        if (SortField.description == sortField)
            comparator = Comparator.comparing(Product::getDescription);
        else if (SortField.price == sortField)
            comparator = Comparator.comparing(Product::getPrice);
        Stream<Product> stream = products.stream();
        if (!Objects.equals(query, "") && query != null) {
            String[] words = query.split(" ");
            comparator = comparator.thenComparing(Product::getDescription, (p1, p2) ->
                    Integer.compare(p1.split(" ").length, p2.split(" ").length));
            stream = stream
                    .filter(product -> Arrays.stream(words).allMatch(word -> product.getDescription().contains(word)));
        }
        if (SortOrder.desc == sortOrder)
            comparator = comparator.reversed();
        return stream
                .filter(product -> product.getPrice() != null)
                .filter(this::productIsInStock)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private boolean productIsInStock(Product product) {
        if (product == null)
            return false;
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
    public synchronized void delete(String id) throws ProductNotFoundException, IdNotFoundException {
        if (id == null)
            throw new IdNotFoundException();
        int size = products.size();
        products = products.stream()
                .filter(x -> !id.equals(x.getId()))
                .collect(Collectors.toList());
        if (size == products.size())
            throw new ProductNotFoundException();
    }
}
