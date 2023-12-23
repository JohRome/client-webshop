package com.jrome.utils;

import com.jrome.httpclient.CustomerAPI;

import java.io.IOException;
import java.net.URISyntaxException;

public class Menu {

    public static void mainMenu(CustomerAPI customerAPI) throws URISyntaxException, IOException, InterruptedException {
        while (true) {
            displayMainMenu();

            int choice = Input.intPut("Enter your choice: ");

            switch (choice) {
                case 1:
                    adminMenu(customerAPI);
                    break;
                case 2:
                    customerMenu(customerAPI);
                    break;
                case 3:
                    showAllProducts(customerAPI);
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminMenu(CustomerAPI customerAPI) {
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
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void customerMenu(CustomerAPI customerAPI) throws URISyntaxException, IOException, InterruptedException {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Show All Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. Show Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Logout");

            int choice = Input.intPut("Enter your choice: ");

            switch (choice) {
                case 1:
                    showAllProducts(customerAPI);
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
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showAllProducts(CustomerAPI customerAPI) {
        try {
            customerAPI.getAllProducts();
        } catch (Exception e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Continue as Admin");
        System.out.println("2. Continue as Customer");
        System.out.println("3. Show All Products");
        System.out.println("4. Exit Program");
    }
}
