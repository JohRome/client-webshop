package com.jrome.httpclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jrome.payload.LoginDTO;
import com.jrome.payload.ProductDTO;
import com.jrome.payload.RegisterDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jrome.httpclient.AuthResponse;
import com.jrome.utils.Input;

public class CustomerAPI {

    private String authToken;
    private String tokenType;

    // Authentication Methods

    /**
     * Attempts to log in the user by sending a POST request with login credentials.
     * If successful, stores the authentication token and token type.
     *
     * @return true if login is successful, false otherwise.
     */
    public boolean login() throws URISyntaxException, IOException, InterruptedException {
        // URL for the login endpoint
        String loginURL = "http://localhost:8080/auth/login";

        // Collect user credentials
        String username = Input.stringPut("Enter your username: ");
        String password = Input.stringPut("Enter your password: ");

        // Create LoginDTO object and convert to JSON
        var loginCredentials = new LoginDTO(username, password);
        Gson gson = new Gson();
        String jsonLoginCredentials = gson.toJson(loginCredentials);

        // Send login request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(new URI(loginURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonLoginCredentials))
                .build();

        // Receive and handle the response
        HttpResponse<String> response = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            // Extract and save the authentication token
            extractAuthToken(response.body());
            System.out.println("\nLogin successful.\n");
            return true;
        } else {
            System.out.println("Login failed. HTTP Status Code: " + response.statusCode());
            return false;
        }
    }

    // Other methods...

    /**
     * Extracts the authentication token and token type from the login response.
     *
     * @param responseBody The response body containing authentication information.
     */
    private void extractAuthToken(String responseBody) {
        Gson gson = new Gson();
        AuthResponse authResponse = gson.fromJson(responseBody, AuthResponse.class);

        // Save the token and token type
        authToken = authResponse.getAccessToken();
        tokenType = authResponse.getTokenType();
    }

    /**
     * Registers a new user by sending a POST request with registration credentials.
     * Prints the response body after registration.
     */
    public void register() throws URISyntaxException, IOException, InterruptedException {
        // URL for the register endpoint
        String registerURL = "http://localhost:8080/auth/register";

        // Collect user credentials
        String username = Input.stringPut("Enter your username: ");
        String password = Input.stringPut("Enter your password: ");

        // Create RegisterDTO object and convert to JSON
        var registerCredentials = new RegisterDTO(username, password);
        Gson gson = new Gson();
        String jsonRegisterCredentials = gson.toJson(registerCredentials);

        // Send register request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest registerRequest = HttpRequest.newBuilder()
                .uri(new URI(registerURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRegisterCredentials))
                .build();

        // Receive and print the registration response
        HttpResponse<String> response = client.send(registerRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    // Product Methods

    /**
     * Retrieves all products by sending a GET request to the products endpoint.
     * Prints the details of fetched products.
     */
    public void getAllProducts() throws URISyntaxException, IOException, InterruptedException {
        // URL for the products endpoint
        String productsURL = "http://localhost:8080/products/";

        // Send GET request to fetch products
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(productsURL))
                .header("Content-Type", "application/json")
                .build();

        // Receive and handle the response
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();

        if (statusCode == 200) {
            // Parse and print the fetched products
            Gson gson = new Gson();
            Type productsType = new TypeToken<ArrayList<ProductDTO>>() {
            }.getType();
            List<ProductDTO> products = gson.fromJson(response.body(), productsType);
            System.out.println("\nAvailable Products:\n");
            for (ProductDTO product : products) {
                System.out.println("Product ID: " + product.getId() + ", Name: " + product.getName() + ", Price: " + product.getPrice());
            }
        } else {
            System.out.println("Error fetching products. Status code: " + statusCode);
        }
    }

    // Cart Methods

    /**
     * Displays the contents of the user's cart by sending a GET request with proper authorization.
     * Prints the cart contents or an error message if the token is missing.
     */
    public void showCart() throws URISyntaxException, IOException, InterruptedException {
        // URL for the cart endpoint
        String cartURL = "http://localhost:8080/cart/";

        // Ensure authToken is not null or empty before making the request
        if (authToken == null || authToken.isEmpty()) {
            System.out.println("Authentication token is missing. Please log in first.");
            return;
        }

        // Send the show cart request using a GET request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest showCartRequest = HttpRequest.newBuilder()
                .uri(new URI(cartURL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();

        // Receive and handle the response
        HttpResponse<String> response = client.send(showCartRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 200) {
            // Print or process the cart products directly from the response body
            System.out.println("\nCart Contents:\n" + response.body());
        } else {
            System.out.println("Error fetching cart. Status code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        }
    }

    /**
     * Adds a product to the user's cart by sending a POST request with product ID and quantity.
     * Prints success message or error details.
     */
    public void addToCart(long productId, int quantity) throws URISyntaxException, IOException, InterruptedException {
        // URL for the addToCart endpoint
        String addToCartURL = "http://localhost:8080/cart/" + productId;

        // Ensure authToken is not null or empty before making the request
        if (authToken == null || authToken.isEmpty()) {
            System.out.println("Authentication token is missing. Please log in first.");
            return;
        }

        // Create a JSON body with the specified quantity
        String requestBody = "{\"quantity\":" + quantity + "}";

        // Send the addToCart request with the JSON body
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest addToCartRequest = HttpRequest.newBuilder()
                .uri(new URI(addToCartURL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Receive and handle the response
        HttpResponse<String> response = client.send(addToCartRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 200) {
            System.out.println("Product added to the cart successfully.");
        } else {
            System.out.println("Error adding product to the cart. Status code: " + response.statusCode());
            System.out.println(response.body());
        }
    }

    /**
     * Removes a product from the user's cart by sending a DELETE request with product ID.
     * Prints success message or error details.
     */
    public void removeFromCart(long productId, int quantity) throws URISyntaxException, IOException, InterruptedException {
        // URL for the removeFromCart endpoint
        String removeFromCartURL = "http://localhost:8080/cart/" + productId;

        // Ensure authToken is not null or empty before making the request
        if (authToken == null || authToken.isEmpty()) {
            System.out.println("Authentication token is missing. Please log in first.");
            return;
        }

        // Create a JSON body with the specified quantity
        String requestBody = "{\"quantity\":" + quantity + "}";

        // Send the removeFromCart request with the JSON body
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest removeFromCartRequest = HttpRequest.newBuilder()
                .uri(new URI(removeFromCartURL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Receive and handle the response
        HttpResponse<String> response = client.send(removeFromCartRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 200) {
            System.out.println("Product removed from the cart successfully. \nAmount removed: " + quantity + " from Product ID: " + productId + ".\n");
        } else {
            System.out.println("Error removing product from the cart. Status code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        }
    }

// Checkout Method

    /**
     * Initiates the checkout process by sending a DELETE request to the checkout endpoint.
     * Prints the response body after checkout.
     */
    public void checkout() throws URISyntaxException, IOException, InterruptedException {
        // URL for the checkout endpoint
        String checkoutURL = "http://localhost:8080/checkout";

        // Send the checkout request using a DELETE request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest checkoutRequest = HttpRequest.newBuilder()
                .uri(new URI(checkoutURL))
                .header("Content-type", "application/json")  // Adjust the content type
                .header("Authorization", "Bearer " + authToken)
                .DELETE()
                .build();

        // Receive and print the checkout response
        HttpResponse<String> checkoutResponse = client.send(checkoutRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("\n" + checkoutResponse.body());
    }
}
