package com.epam.training.fooddelivery.service;

import com.epam.training.fooddelivery.data.DataStore;
import com.epam.training.fooddelivery.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class DefaultFoodDeliveryService implements FoodDeliveryService {

    @Autowired
    @Qualifier("fileDataStore")
    private DataStore dataStore;
    private int priceCart;


    public Customer authenticate(User user) {

        if (dataStore.getCustomers().stream()
                .anyMatch(customer ->
                        customer.getEmail().
                                equals(user.getEmail()) && customer.getPassword()
                                .equals(user.getPassword())))
            return dataStore.getCustomers().stream().filter(customer -> customer.getPassword()
                    .equals(user.getPassword())).collect(Collectors.toList()).get(0);
        else {
            throw new AuthenticationException("User it's not found");
        }
    }

    public List<Food> listAllFood() {
        return dataStore.getFoods();
    }

    public void updateCart(Customer customer, Food food, int pieces) {

        OrderItem orderItem = new OrderItem();
        orderItem.setPieces(pieces);
        orderItem.setFood(food);
        orderItem.setPrice(food.getPrice().multiply(new BigDecimal(pieces)));
        // Verificam daca exista produse in cos
        if (!customer.getCart().getOrderItems().isEmpty()) {

            List<OrderItem> orderItems = customer.getCart().getOrderItems();
            //Eliminam produse din cos
            if (pieces == 0) {
                orderItems.removeIf(orderItem1 -> orderItem1.getFood() == food);
                priceCart = 0;
                orderItems.forEach(orderItem1 -> priceCart += orderItem1.getPrice().intValue());
                customer.getCart().setPrice(new BigDecimal(priceCart));
            }
            //Update la produse existente in cos
            else if (orderItems.stream().anyMatch(orderItem1 -> orderItem1.getFood() == food)) {

                //  Cauta orderitem daca exista update la element
                int index = orderItems.indexOf(orderItems.stream()
                        .filter(orderItem1 -> orderItem1.getFood().equals(food))
                        .collect(Collectors.toList()).get(0));
                orderItems.set(index, orderItem);
                customer.getCart().setOrderItems(orderItems);
                priceCart = 0;
                orderItems.forEach(orderItem1 -> priceCart += orderItem1.getPrice().intValue());
                customer.getCart().setPrice(BigDecimal.valueOf(priceCart));

            }
            //Adaugam produse noi in cosul existent
            else {

                orderItems.add(orderItem);
                customer.getCart().setOrderItems(orderItems);
                customer.getCart().setPrice(food.getPrice().multiply(new BigDecimal(pieces)).add(customer.getCart().getPrice()));
            }
        }
        //Facem un nou cos
        else {
            Cart cart = new Cart();
            List<OrderItem> orderItems = new ArrayList<>();
            orderItems.add(orderItem);
            cart.setPrice(BigDecimal.valueOf((long) food.getPrice().intValue() * pieces));
            cart.setOrderItems(orderItems);
            customer.setCart(cart);
        }
    }

    public Order createOrder(Customer customer) {

        if (customer.getCart().getPrice().intValue() == 0)
            throw new IllegalStateException();

        if (customer.getCart().getPrice().intValue() > customer.getBalance().intValue())
            throw new LowBalanceException("You don't have enough money, your balance is only " + customer.getBalance() + " EUR. " +
                    "We do not empty your cart, please remove some of the items.");

        Order order = new Order();
        order.setCustomerId(customer.getId());
        order.setOrderItems(customer.getCart().getOrderItems());
        order.setTimestampCreated(LocalDateTime.now());
        order.setPrice(customer.getCart().getPrice());
        Long orderId = dataStore.getOrders().stream().max(Comparator.comparing(Order::getOrderId)).get().getOrderId() + 1;
        order.setOrderId(orderId);

        customer.setCart(new Cart());
        List<Order> orders;
        if (customer.getOrders().size() > 0) {
            orders = customer.getOrders();
        } else {
            orders = new ArrayList<>();
        }
        orders.add(order);
        customer.setOrders(orders);

        return dataStore.createOrder(order);
    }
}
