package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Arrays;
import java.util.Currency;
import java.util.Map;
import java.util.List;


public class Product {
    private String id;
    private String code;
    private String description;
    private BigDecimal price;
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<Map.Entry<String, BigDecimal>> priceHistory;

    public Product() {
    }

    public static class Builder {
        private Product product;

        public Builder() {
            product = new Product();
            product.priceHistory = new ArrayList<>();
        }

        public Builder withId(String id) {
            product.id = id;
            return this;
        }

        public Builder withCode(String code) {
            product.code = code;
            return this;
        }

        public Builder withDescription(String description) {
            product.description = description;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            product.price = price;
            return this;
        }

        public Builder withCurrency(Currency currency) {
            product.currency = currency;
            return this;
        }

        public Builder withStock(int stock) {
            product.stock = stock;
            return this;
        }

        public Builder withImageUrl(String imageUrl) {
            product.imageUrl = imageUrl;
            return this;
        }

        public Builder withPriceHistory(Map.Entry<String, BigDecimal>... priceHistory) {
            product.priceHistory.addAll(Arrays.asList(priceHistory));
            return this;
        }

        public Product build() {
            if (product.getId() == null)
                product.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            return product;
        }
    }

    public List<Map.Entry<String, BigDecimal>> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<Map.Entry<String, BigDecimal>> priceHistory) {
        this.priceHistory = priceHistory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}