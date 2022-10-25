package com.epam.training.fooddelivery.view;

import com.epam.training.fooddelivery.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
@Component
public class CLIView implements View {
    private final Scanner keyboard = new Scanner(System.in);
    private String foodName;

    @Override
    public User readCredentials() {
        System.out.print("Enter customer email address:");
        String email = keyboard.next();
        System.out.print("Enter customer email password:");
        String pass = keyboard.next();
        User user = new User();
        user.setEmail(email);
        user.setPassword(pass);
        return user;
    }

    @Override
    public void printWelcomeMessage(Customer customer) {
        System.out.println("Welcome, " + customer.getName() + ". Your balance is: " + customer.getBalance() + " EUR\n");
        System.out.println("These are our goods today:");
    }

    @Override
    public void printAllFoods(List<Food> foods) {

        foods.forEach(food -> System.out.println("- " + food.getName() + " " + food.getPrice() + " EUR each"));

    }

    @Override
    public Food selectFood(List<Food> foods) {

        System.out.print("\n");
        System.out.print("Please enter the name of the food you would like to buy or delete from the cart:");
        foodName = keyboard.next();

        while (foods.stream().noneMatch(food1 -> food1.getName().equals(foodName))) {
            printErrorMessage("Invalid input.");
            System.out.print("Please enter the name of the food you would like to buy or delete from the cart:");
            foodName = keyboard.next();
        }

        return foods.stream().filter(food1 -> food1.getName().equals(foodName)).collect(Collectors.toList()).get(0);
    }

    @Override
    public int readPieces() {
        System.out.print("How many pieces do you want to buy? This input overwrites the old value in the cart, entering zero removes the\n" +
                "item completely:");
        return keyboard.nextInt();
    }

    @Override
    public void printAddedToCart(Food food, int pieces) {
        System.out.printf("Added %d piece(s) of %s to the cart.%n", pieces, food.getName());
    }

    @Override
    public void printCart(Cart cart) {
        System.out.printf("The cart has %s EUR of foods:%n", cart.getPrice());
        cart.getOrderItems().forEach(orderItem ->
                System.out.println(orderItem.getFood().getName() + " " + orderItem.getPieces() + " piece(s), " +
                        orderItem.getPrice() + " EUR total"));
    }

    @Override
    public void printConfirmOrder(Order order) {
        System.out.println("Order (items: " + order.getOrderItems().toString() + ", price: " + order.getPrice() + " EUR, timestamp: " + order.getTimestampCreated() + ") has been confirmed. " +
                "Thank you for your purchase.");
    }

    @Override
    public boolean promptOrder() {
        System.out.print("Are you finished with your order? (y/n)");
        String finish = keyboard.next();

        while (!(finish.equals("y") || finish.equals("n"))) {
            printErrorMessage("Invalid input.");
            System.out.print("Are you finished with your order? (y/n)");
            finish = keyboard.next();
        }

        return finish.equals("y");

    }

    @Override
    public void printErrorMessage(String message) {
        System.out.println(message);
    }
}
