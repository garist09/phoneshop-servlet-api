package com.es.phoneshop.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class PriceHistoryItem implements Serializable {
    private String startDate;
    private BigDecimal price;

    public PriceHistoryItem() {
    }

    public PriceHistoryItem(String startDate, BigDecimal price) {
        this.startDate = startDate;
        this.price = price;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getStartDate() {
        return startDate;
    }

    @Override
    public boolean equals(Object secondObject) {
        if (this == secondObject) {
            return true;
        }
        if (secondObject == null || getClass() != secondObject.getClass()) {
            return false;
        }
        PriceHistoryItem secondPriceHistoryItem = (PriceHistoryItem) secondObject;
        return Objects.equals(startDate, secondPriceHistoryItem.startDate)
                && Objects.equals(price, secondPriceHistoryItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, price);
    }
}
