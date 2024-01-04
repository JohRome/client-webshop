package com.jrome.httpclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jrome.payload.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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
     * @return true if login is successful, false otherwise.
     */
    public boolean login() throws URISyntaxException, IOException, InterruptedException {
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

        HttpResponse<String> response = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response
        if (response.statusCode() == 200) {
            extractAuthToken(response.body());
            System.out.println("\nLogin successful.\n");
            return true;
        } else {
            System.out.println("Login failed. HTTP Status Code: " + response.statusCode());
            return false;
        }
    }

    /**
     * Extracts the authentication token and token type from the login response.
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

        HttpResponse<String> response = client.send(registerRequest, HttpResponse.BodyHandlers.ofString());

        // Print the registration response
        System.out.println(response.body());
    }

    // Product Methods

    /**
     * Retrieves all products by sending a GET request to the products endpoint.
     * Prints the details of fetched products.
     */
    public void getAllProducts() throws URISyntaxException, IOException, InterruptedException {
        String productsURL = "http://localhost:8080/products/";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(productsURL))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();

        if (statusCode == 200) {
            Gson gson = new Gson();
            Type productsType = new TypeToken<ArrayList<ProductDTO>>() {}.getType();
            List<ProductDTO> products = gson.fromJson(response.body(), productsType);

            // Print or process the fetched products
            System.out.println("Fetched Products:");
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

        HttpResponse<String> response = client.send(showCartRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 200) {
            // Print or process the cart products directly from the response body
            System.out.println("Cart Contents:\n" + response.body());
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

        HttpResponse<String> response = client.send(addToCartRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 200) {
            System.out.println("Product added to the cart successfully.");
        } else {
            System.out.println("Error adding product to the cart. Status code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        }
    }

    /**
     * Removes a product from the user's cart by sending a DELETE request with product ID.
     * Prints success message or error details.
     */
    public void removeFromCart(long productId, int quantity) throws URISyntaxException, IOException, InterruptedException {
        String removeFromCartURL = "http://localhost:8080/cart/" + productId;

        // Ensure authToken is not null or empty before making the request
        if (authToken == null || authToken.isEmpty()) {
            System.out.println("Authentication token is missing. Please log in first.");
            return;
        }

        // Send the removeFromCart request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest removeFromCartRequest = HttpRequest.newBuilder()
                .uri(new URI(removeFromCartURL))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(removeFromCartRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 200) {
            System.out.println("Product removed from the cart successfully. Amount removed: "+ quantity);
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
        String checkoutURL = "http://localhost:8080/checkout";

        // Continue with the checkout logic
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest checkoutRequest = HttpRequest.newBuilder()
                .uri(new URI(checkoutURL))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .DELETE()
                .build();

        HttpResponse<String> checkoutResponse = client.send(checkoutRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the checkout response as needed
        System.out.println("Checkout Response: " + checkoutResponse.body());
    }

    // Admin Methods

    /**
     * Adds a new product as an admin by sending a POST request with product details.
     * Prints success message or error details.
     */
    public void addProductAsAdmin(String productName, double productCost, String productDesc)
            throws URISyntaxException, IOException, InterruptedException {

        String adminURL = "http://localhost:8080/products/admin";

        // Ensure authToken is not null or empty before making the request
        if (authToken == null || authToken.isEmpty()) {
            System.out.println("Authentication token is missing. Please log in first.");
            return;
        }

        // Create a product object and convert to JSON
        var product = new ProductDTO();
        product.setName(productName);
        product.setPrice(productCost);
        product.setDescription(productDesc);

        Gson gson = new Gson();
        String jsonProduct = gson.toJson(product);

        HttpClient client = HttpClient.newHttpClient();

        // Send the addProduct request
        HttpRequest addProductRequest = HttpRequest.newBuilder()
                .uri(URI.create(adminURL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonProduct))
                .build();

        HttpResponse<String> response = client.send(addProductRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 200) {
            System.out.println("Product added successfully.");

            // Print additional information if necessary
            int statusCode = response.statusCode();
            String responseBody = response.body();
            var convertedBody = gson.fromJson(responseBody, ProductDTO.class);

            System.out.println("Status Code: " + statusCode);
            System.out.println("Converted Body: " + convertedBody);
        } else {
            System.out.println("Error adding product. Status code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        }
    }
}
