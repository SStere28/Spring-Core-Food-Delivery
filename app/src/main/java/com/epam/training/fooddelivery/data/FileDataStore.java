package com.epam.training.fooddelivery.data;

import com.epam.training.fooddelivery.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@PropertySource(value = "classpath:/application.properties")
public class FileDataStore implements DataStore {
    private static final int CUSTOMER_EMAIL = 0;
    private static final int CUSTOMER_PASSWORD = 1;
    private static final int CUSTOMER_ID = 2;
    private static final int CUSTOMER_NAME = 3;
    private static final int CUSTOMER_BALANCE = 4;
    private static final int CUSTOMER_CART = 5;
    private static final int FOOD_NAME = 0;
    private static final int FOOD_CALORIE = 1;
    private static final int FOOD_DESCRIPTION = 2;
    private static final int FOOD_PRICE = 3;
    private static final int FOOD_CATEGORY = 4;
    private static final int ORDERID = 0;
    private static final int CUSTOMERID = 1;
    private static final int ORDERITEMNAME = 2;
    private static final int ORDERITEMPIECES = 3;
    private static final int ORDERITEMPPRICE = 4;
    private static final int TIMESTAMP = 5;
    @Value("${baseDirPath}")
    private String baseDirPath;

    public List<Order> orders;
    public List<Customer> customers;

    public List<Food> foods;


    public void init() {

        foods = getFoods();
        customers = getCustomers();
        orders = readFromFileForOrders();

    }

    public List<Customer> getCustomers() {
        List customers1 = new ArrayList<>();
        try (InputStream inputStream = Files.newInputStream(Path.of(baseDirPath + File.separator + "customers.csv"));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            bufferedReader.lines().forEach(e -> {
                String[] s = e.split(",");
                Customer customer = new Customer();
                customer.setEmail(s[CUSTOMER_EMAIL]);
                customer.setPassword(s[CUSTOMER_PASSWORD]);
                customer.setId(Long.parseLong(s[CUSTOMER_ID]));
                customer.setName(s[CUSTOMER_NAME]);
                customer.setBalance(new BigDecimal(s[CUSTOMER_BALANCE]));

                Cart cart = new Cart();
                if (s.length > 5) {
                    cart.setPrice(new BigDecimal(s[CUSTOMER_CART]));
                } else cart.setPrice(BigDecimal.valueOf(0));
                cart.setOrderItems(new ArrayList<>());
                customer.setCart(cart);

                customer.setOrders(getOrders().stream().filter(order -> order.getCustomerId() == Long.parseLong(s[CUSTOMER_ID])).collect(Collectors.toList()));

                customers1.add(customer);
            });


        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return customers1;
    }

    public List<Food> getFoods() {
        List<Food> foods1 = new ArrayList<>();
        try (InputStream inputStream = Files.newInputStream(Path.of(baseDirPath + File.separator + "foods.csv"));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            bufferedReader.lines().forEach(e -> {
                String[] s = e.split(",");
                Food food = new Food();
                food.setName(s[FOOD_NAME]);
                food.setCalorie(new BigDecimal(s[FOOD_CALORIE]));
                food.setDescription((s[FOOD_DESCRIPTION]));
                food.setPrice(new BigDecimal(s[FOOD_PRICE]));
                food.setCategory(Category.valueOf(s[FOOD_CATEGORY]));
                foods1.add(food);
            });


        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return foods1;
    }

    public List<Order> getOrders() {
        if (orders == null) {
            orders = readFromFileForOrders();
            return orders;
        } else return orders;
    }

    private List<Order> readFromFileForOrders() {
        List<Order> orders1 = new ArrayList<>();
        List<String> listClean = new ArrayList<>();
        try (InputStream inputStream = Files.newInputStream(Path.of(baseDirPath + File.separator + "orders.csv"));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            listClean = bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        listClean.forEach(list -> {
            OrderItem orderItem = new OrderItem();
            String[] orderValues = list.split(",");

            int orderID = Integer.parseInt(orderValues[ORDERID]);
            int customerID = Integer.parseInt(orderValues[CUSTOMERID]);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(orderValues[TIMESTAMP], dateTimeFormatter);

            Optional<Order> orderByID = findOrderByID(orders1, orderID);
            Order order = orderByID.orElseGet(Order::new);

            orderItem.setPrice(new BigDecimal(orderValues[ORDERITEMPPRICE]));
            orderItem.setPieces(Integer.parseInt(orderValues[ORDERITEMPIECES]));

            orderItem.setFood(getFoods().stream()
                    .filter(food1 -> Objects.equals(food1.getName(), orderValues[ORDERITEMNAME]))
                    .collect(Collectors.toList()).get(0));

            List<OrderItem> orderItems = order.getOrderItems();

            if (orderItems != null) {
                orderItems.add(orderItem);

            } else {
                orderItems = new ArrayList<>();
                orderItems.add(orderItem);

            }

            if (orderByID.isEmpty()) {
                order.setOrderId((long) orderID);
                order.setOrderItems(orderItems);
                order.setCustomerId(customerID);
                order.setPrice(BigDecimal.valueOf(Integer.parseInt(orderValues[ORDERITEMPPRICE])));
                order.setTimestampCreated(localDateTime);
                orders1.add(order);
            } else {
                order.setPrice(BigDecimal.valueOf(Long.parseLong(orderValues[ORDERITEMPPRICE])).add(order.getPrice()));
            }

        });
        return orders1;
    }

    @Override
    public Order createOrder(Order order) {
        customers = getCustomers();
        orders = getOrders();

        Customer customer;
        customer = customers.stream()
                .filter(customer1 -> customer1.getId() == order.getCustomerId())
                .collect(Collectors.toList()).get(0);
        int index = customers.indexOf(customers.stream()
                .filter(customer1 -> customer1.getId() == customer.getId()).collect(Collectors.toList()).get(0));
        List<Order> orders1 = new ArrayList<>();
        if (customer.getOrders().size() == 0) {
            orders1.add(order);
            customer.setOrders(orders1);
        } else {
            orders1 = customer.getOrders();
            orders1.add(order);
        }
        customers.set(index, customer);

        orders.add(order);
        return order;
    }

    private Optional<Order> findOrderByID(List<Order> orders, int orderID) {
        return orders.stream().filter(o -> o.getOrderId() == orderID).findFirst();
    }

}
