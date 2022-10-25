package com.epam.training.fooddelivery.service;

import com.epam.training.fooddelivery.domain.Customer;
import com.epam.training.fooddelivery.domain.Food;
import com.epam.training.fooddelivery.domain.Order;
import com.epam.training.fooddelivery.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface FoodDeliveryService {

    Customer authenticate(User user);

    List<Food> listAllFood();

    void updateCart(Customer customer, Food food, int pieces);

    Order createOrder(Customer customer);
}
