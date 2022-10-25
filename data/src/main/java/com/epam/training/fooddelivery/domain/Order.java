package com.epam.training.fooddelivery.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long orderId;
    private long customerId;
    private BigDecimal price;
    private LocalDateTime timestampCreated;
    private List<OrderItem> orderItems;

    public Order() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(LocalDateTime timestampCreated) {
        this.timestampCreated = timestampCreated;
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
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getCustomerId() == order.getCustomerId() && Objects.equals(getOrderId(), order.getOrderId()) && Objects.equals(getPrice(), order.getPrice()) && Objects.equals(getTimestampCreated(), order.getTimestampCreated()) && Objects.equals(getOrderItems(), order.getOrderItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getCustomerId(), getPrice(), getTimestampCreated(), getOrderItems());
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", price=" + price +
                ", timestampCreated=" + timestampCreated +
                ", orderItems=" + orderItems +
                '}';
    }
}
