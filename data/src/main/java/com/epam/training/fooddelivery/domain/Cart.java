package com.epam.training.fooddelivery.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Cart {
    private BigDecimal price;
    private List<OrderItem> orderItems;

    public Cart() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart cart = (Cart) o;
        return Objects.equals(getPrice(), cart.getPrice()) && Objects.equals(getOrderItems(), cart.getOrderItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrice(), getOrderItems());
    }

    @Override
    public String toString() {
        return "Cart{" +
                "price=" + price +
                ", orderItems=" + orderItems +
                '}';
    }
}
