package com.epam.training.fooddelivery.data;

import com.epam.training.fooddelivery.domain.Customer;
import com.epam.training.fooddelivery.domain.Food;
import com.epam.training.fooddelivery.domain.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DataStore {
    List<Customer> getCustomers();

    List<Food> getFoods();

    List<Order> getOrders();

    Order createOrder(Order order);

    void init();
}
