package com.es.phoneshop.model.product;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.UUID;


public class Product implements Serializable {
    private static final int RANDOM_SEED = 1;
    private static final Random random;

    private String id;
    private String code;
    private String description;
    private BigDecimal price;
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistoryItem> priceHistory;

    static {
        random = new Random(RANDOM_SEED);
    }

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

        public Builder withPriceHistory(PriceHistoryItem... priceHistory) {
            product.priceHistory.addAll(Arrays.asList(priceHistory));
            return this;
        }

        public Product build() {
            if (StringUtils.isBlank(product.getId())) {
                product.setId(new UUID(random.nextLong(),random.nextLong()).toString());
            }
            return product;
        }
    }

    public List<PriceHistoryItem> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<PriceHistoryItem> priceHistory) {
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

    @Override
    public boolean equals(Object secondObject) {
        if (this == secondObject) {
            return true;
        }
        if (secondObject == null || getClass() != secondObject.getClass()) {
            return false;
        }
        Product product = (Product) secondObject;
        return stock == product.stock && Objects.equals(id, product.id) && Objects.equals(code, product.code)
                && Objects.equals(description, product.description) && Objects.equals(price, product.price)
                && Objects.equals(currency, product.currency) && Objects.equals(imageUrl, product.imageUrl)
                && Objects.equals(priceHistory, product.priceHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, price, currency, stock, imageUrl, priceHistory);
    }
}