package com.epam.training.fooddelivery.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Customer extends User {
    private long id;
    private String name;
    private BigDecimal balance;
    private List<Order> orders;
    private Cart cart;


    public Customer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return getId() == customer.getId() && Objects.equals(getName(), customer.getName()) && Objects.equals(getBalance(), customer.getBalance()) && Objects.equals(getOrders(), customer.getOrders()) && Objects.equals(getCart(), customer.getCart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getName(), getBalance(), getOrders(), getCart());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", orders=" + orders +
                ", cart=" + cart +
                '}';
    }
}
