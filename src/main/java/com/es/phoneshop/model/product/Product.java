package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Currency;

public class Product {
    private Long id;
    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private String imageUrl;

    public Product() {
    }

    public static class Builder {
        private Product product;

        public Builder() {
            product = new Product();
        }

        public Builder withId(Long id) {
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

        public Product build() {
            if (product.getId() == null)
            {
                if (product.getCode() != null)
                    product.setId((long) product.getCode().hashCode());
                else
                    product.setId((long) (Math.random() * 1000 + 1));
            }
            return product;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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