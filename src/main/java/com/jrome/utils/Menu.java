package com.jrome.utils;

import com.jrome.httpclient.CustomerAPI;
import com.jrome.httpclient.AdminAPI;
import java.io.IOException;
import java.net.URISyntaxException;

public class Menu {

    private static final CustomerAPI customerAPI = new CustomerAPI();
    private static final AdminAPI adminAPI = new AdminAPI();

    /**
     * Main menu for the application, allowing users to choose between admin, customer, or exit options.
     */
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

    /**
     * Admin menu, providing login, CRUD operations, and logout options.
     */
    private static void adminMenu() throws URISyntaxException, IOException, InterruptedException {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Login");
            System.out.println("2. CRUD Product List");
            System.out.println("3. Logout");

            int choice = Input.intPut("Enter your choice: ");

            switch (choice) {
                case 1:
                    adminAPI.login();
                    break;
                case 2:
                    adminCRUDMenu();
                    break;
                case 3:
                    // Implement logout logic if needed
                    return;
                default:
                    displayError("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Admin CRUD menu, providing options for adding, updating, and deleting products.
     */
    private static void adminCRUDMenu() throws URISyntaxException, IOException, InterruptedException {
        while (true) {
            System.out.println("\nAdmin CRUD Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Back to Admin Menu");

            int choice = Input.intPut("Enter your choice: ");

            switch (choice) {
                case 1:
                    adminAPI.addProductAsAdmin(Input.stringPut("Enter Product Name: "),
                            Input.intPut("Enter Product Cost: "),
                            Input.stringPut("Enter Product Description: "));
                    break;
                case 2:
                    adminAPI.updateProductAsAdmin(Input.stringPut("Enter Product ID:"));// Implement logic to update a product
                    break;
                case 3:
                    adminAPI.deleteProductAsAdmin(Input.stringPut("Enter Product ID: "));// Implement logic to delete a product
                    break;
                case 4:
                    return; // Back to Admin Menu
                default:
                    displayError("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Customer menu, allowing login, registration, and various product-related operations.
     */
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

            System.out.println("You are now logged in. Choose an option:");
            System.out.println("1. Show All Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. Remove Product from Cart");
            System.out.println("4. Show Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Logout");

            int choice = Input.intPut("Enter your choice: ");

            switch (choice) {
                case 1:
                    showAllProducts();
                    break;
                case 2:
                    long productId = Input.intPut("Enter the product ID to add to the cart: ");
                    int productQty = Input.intPut("Enter the amount you want to add to the cart: ");
                    customerAPI.addToCart(productId, productQty);
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

    /**
     * Displays all products available.
     */
    private static void showAllProducts() {
        try {
            customerAPI.getAllProducts();
        } catch (Exception e) {
            displayError("Error fetching products: " + e.getMessage());
        }
    }

    /**
     * Displays the main menu options.
     */
    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Continue as Admin");
        System.out.println("2. Continue as Customer");
        System.out.println("3. Show All Products");
        System.out.println("4. Exit Program");
    }

    /**
     * Displays an error message.
     * @param message The error message to be displayed.
     */
    private static void displayError(String message) {
        System.out.println("Error: " + message);
    }
}