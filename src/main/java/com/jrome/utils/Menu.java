package com.jrome.utils;

import com.jrome.httpclient.CustomerAPI;


import java.io.IOException;
import java.net.URISyntaxException;

public class Menu {

    private static final CustomerAPI customerAPI = new CustomerAPI();

    public static void mainMenu() throws URISyntaxException, IOException, InterruptedException {
        while (true) {
            displayMainMenu();

            int choice = Input.intPut("Enter your choice: ");

            switch (choice) {
                case 1:
                    adminMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                case 3:
                    showAllProducts();
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    System.exit(0);
                default:
                    displayError("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. CRUD Product List");
            System.out.println("2. Logout");

            int choice = Input.intPut("Enter your choice: ");

            switch (choice) {
                case 1:
                    System.out.println("This is where CRUD operations will be performed.");
                    break;
                case 2:
                    return;
                default:
                    displayError("Invalid choice. Please try again.");
            }
        }
    }

    private static void customerMenu() throws URISyntaxException, IOException, InterruptedException {
        boolean loggedIn = false;

        while (true) {
            System.out.println("\nCustomer Menu:");

            if (!loggedIn) {
                System.out.println("You need to log in or register first:");
                System.out.println("1. Log In");
                System.out.println("2. Register");
                System.out.println("3. Back to Main Menu");

                int loginChoice = Input.intPut("Enter your choice: ");

                switch (loginChoice) {
                    case 1:
                        loggedIn = customerAPI.login();
                        break;
                    case 2:
                         customerAPI.register();
                        break;
                    case 3:
                        return; // Back to Main Menu
                    default:
                        displayError("Invalid choice. Please try again.");
                }

                if (!loggedIn) {
                    continue; // Re-display Customer Menu if login or register fails
                }
            }

            // Once logged in, display CRUD options
            System.out.println("You are now logged in. Choose an option:");
            System.out.println("1. Show All Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. Show Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Logout");

            int choice = Input.intPut("Enter your choice: ");

            switch (choice) {
                case 1:
                    showAllProducts();
                    break;
                case 2:
                    long productId = Input.intPut("Enter the product ID to add to the cart:");
                    customerAPI.addToCart(productId);
                    break;
                case 3:
                    customerAPI.showCart();
                    break;
                case 4:
                    customerAPI.checkout();
                    break;
                case 5:
                    loggedIn = false; // Logout
                    return;
                default:
                    displayError("Invalid choice. Please try again.");
            }
        }
    }

    private static void showAllProducts() {
        try {
            customerAPI.getAllProducts();
        } catch (Exception e) {
            displayError("Error fetching products: " + e.getMessage());
        }
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Continue as Admin");
        System.out.println("2. Continue as Customer");
        System.out.println("3. Show All Products");
        System.out.println("4. Exit Program");
    }

    private static void displayError(String message) {
        System.out.println("Error: " + message);
    }
}
