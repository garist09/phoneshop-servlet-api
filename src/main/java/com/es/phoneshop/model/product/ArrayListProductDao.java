package com.es.phoneshop.model.product;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private List<Product> products;

    public ArrayListProductDao() {
        this.products = new ArrayList<>();
        saveSampleProducts();
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

    private void saveSampleProducts() {
        Currency usd = Currency.getInstance("USD");
        save(new Product.Builder().withCode("sgs").withDescription("Samsung Galaxy S").withPrice(new BigDecimal(100)).withCurrency(usd).withStock(100).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg").build());
        save(new Product.Builder().withCode("sgs2").withDescription("Samsung Galaxy S II").withPrice(new BigDecimal(200)).withCurrency(usd).withStock(0).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg").build());
        save(new Product.Builder().withCode("sgs3").withDescription("Samsung Galaxy S III").withPrice(new BigDecimal(300)).withCurrency(usd).withStock(5).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg").build());
        save(new Product.Builder().withCode("iphone").withDescription("Apple iPhone").withPrice(new BigDecimal(200)).withCurrency(usd).withStock(10).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg").build());
        save(new Product.Builder().withCode("iphone6").withDescription("Apple iPhone 6").withPrice(new BigDecimal(1000)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg").build());
        save(new Product.Builder().withCode("htces4g").withDescription("HTC EVO Shift 4G").withPrice(new BigDecimal(320)).withCurrency(usd).withStock(3).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg").build());
        save(new Product.Builder().withCode("sec901").withDescription("Sony Ericsson C901").withPrice(new BigDecimal(420)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg").build());
        save(new Product.Builder().withCode("xperiaxz").withDescription("Sony Xperia XZ").withPrice(new BigDecimal(100)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg").build());
        save(new Product.Builder().withCode("nokia3310").withDescription("Nokia 3310").withPrice(new BigDecimal(70)).withCurrency(usd).withStock(100).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg").build());
        save(new Product.Builder().withCode("palmp").withDescription("Palm Pixi").withPrice(new BigDecimal(170)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg").build());
        save(new Product.Builder().withCode("simc56").withDescription("Siemens C56").withPrice(new BigDecimal(70)).withCurrency(usd).withStock(20).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg").build());
        save(new Product.Builder().withCode("simc61").withDescription("Siemens C61").withPrice(new BigDecimal(80)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg").build());
        save(new Product.Builder().withCode("simsxg75").withDescription("Siemens SXG75").withPrice(new BigDecimal(150)).withCurrency(usd).withStock(40).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg").build());
    }
}
