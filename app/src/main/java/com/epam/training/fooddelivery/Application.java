package com.epam.training.fooddelivery;

import com.epam.training.fooddelivery.data.DataStore;
import com.epam.training.fooddelivery.domain.Customer;
import com.epam.training.fooddelivery.domain.Food;
import com.epam.training.fooddelivery.service.AuthenticationException;
import com.epam.training.fooddelivery.service.FoodDeliveryService;
import com.epam.training.fooddelivery.view.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Application {



    @Autowired
    @Qualifier("defaultFoodDeliveryService")
    private FoodDeliveryService foodDeliveryService;

    @Autowired
    private View view;

    public Application() {
    }

    public void run() {
        Customer customer = null;
        Food food;
        int pieces;
        boolean confirmOrder;
        try {
            customer = foodDeliveryService.authenticate(view.readCredentials());
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        view.printWelcomeMessage(customer);

        do {

            view.printAllFoods(foodDeliveryService.listAllFood());
            food = view.selectFood(foodDeliveryService.listAllFood());
            pieces = view.readPieces();
            foodDeliveryService.updateCart(customer, food, pieces);
            view.printAddedToCart(food, pieces);
            view.printCart(customer.getCart());
            confirmOrder = view.promptOrder();

            if (confirmOrder) {
                try {
                    view.printConfirmOrder(foodDeliveryService.createOrder(customer));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    confirmOrder = false;
                }
            }

        } while (!confirmOrder);

    }

}
