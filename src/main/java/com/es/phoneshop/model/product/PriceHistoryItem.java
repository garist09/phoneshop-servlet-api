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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceHistoryItem that = (PriceHistoryItem) o;
        return Objects.equals(startDate, that.startDate) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, price);
    }
}
