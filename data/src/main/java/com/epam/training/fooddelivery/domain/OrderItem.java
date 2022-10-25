package com.epam.training.fooddelivery.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {
    private int pieces;
    private BigDecimal price;
    private Food food;

    public OrderItem() {
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem orderItem = (OrderItem) o;
        return getPieces() == orderItem.getPieces() && Objects.equals(getPrice(), orderItem.getPrice()) && Objects.equals(getFood(), orderItem.getFood());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPieces(), getPrice(), getFood());
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "pieces=" + pieces +
                ", price=" + price +
                ", food=" + food +
                '}';
    }
}
