package com.jrome.httpclient;

import com.google.gson.Gson;
import com.jrome.payload.LoginDTO;
import com.jrome.payload.ProductDTO;
import com.jrome.utils.Input;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AdminAPI {

    private String authToken;
    private String tokenType;
    private boolean isAdminLoggedIn = false;

    // ... (unchanged methods)

    /**
     * Attempts to log in the admin by sending a POST request with login credentials.
     * If successful, stores the authentication token.
     */
    public void login() throws URISyntaxException, IOException, InterruptedException {
        String loginURL = "http://localhost:8080/auth/login";

        // Check if the admin is already logged in
        if (isAdminLoggedIn) {
            System.out.println("You are already logged in.");
            return;
        }

        // Collect admin credentials
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
            System.out.println(authToken);
            isAdminLoggedIn = true;  // Set the login status
        } else {
            System.out.println("Login failed. HTTP Status Code: " + response.statusCode());
        }
    }
    private void extractAuthToken(String responseBody) {
        Gson gson = new Gson();
        AuthResponse authResponse = gson.fromJson(responseBody, AuthResponse.class);

        // Save the token and token type
        authToken = authResponse.getAccessToken();
        tokenType = authResponse.getTokenType();
    }

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


    public void updateProductAsAdmin(String id) throws URISyntaxException, IOException, InterruptedException {
        if (!isAdminLoggedIn) {
            System.out.println("You need to log in as an admin first.");
            return;
        }

        // Collect updated product details
        String name = Input.stringPut("Enter the updated product name: ");
        String description = Input.stringPut("Enter the updated product description: ");
        double price = Input.doublePut("Enter the updated product price: ");

        // Create a product object with the updated details
        var updatedProduct = new ProductDTO();
        updatedProduct.setName(name);
        updatedProduct.setDescription(description);
        updatedProduct.setPrice(price);

        // Convert the product object to JSON
        Gson gson = new Gson();
        String jsonUpdatedProduct = gson.toJson(updatedProduct);

        // Construct the update product URL
        String updateProductURL = "http://localhost:8080/products/" + id;

        // Send the update product request with the authentication token
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest updateProductRequest = HttpRequest.newBuilder()
                .uri(new URI(updateProductURL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonUpdatedProduct))
                .build();

        HttpResponse<String> response = client.send(updateProductRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response
        if (response.statusCode() == 200) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Error updating product. Status code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        }
    }

    public void deleteProductAsAdmin(String id) throws URISyntaxException, InterruptedException {
        String deleteProductURL = "http://localhost:8080/products/" + id;

        // Ensure authToken is not null or empty before making the request
        if (authToken == null || authToken.isEmpty()) {
            System.out.println("Authentication token is missing. Please log in first.");
            return;
        }

        // Send the deleteProduct request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest deleteProductRequest = HttpRequest.newBuilder()
                .uri(new URI(deleteProductURL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(deleteProductRequest, HttpResponse.BodyHandlers.ofString());

            // Handle the response status
            if (response.statusCode() == 200) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Error deleting product. Status code: " + response.statusCode());
                System.out.println("Response Body: " + response.body());
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
