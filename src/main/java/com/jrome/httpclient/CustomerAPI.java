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

    private String authToken; // Instance variable to store the authentication token
    private String tokenType; // Instance variable to store the token type

    // Auth Related

    public void login() throws URISyntaxException, IOException, InterruptedException {
        String loginURL = "http://localhost:8080/auth/login";

        // Get user inputs for username and password
        String username = Input.stringPut("Enter your username: ");
        String password = Input.stringPut("Enter your password: ");

        // Create a LoginDTO object
        var loginCredentials = new LoginDTO(username, password);

        // Convert the LoginDTO to JSON
        Gson gson = new Gson();
        String jsonLoginCredentials = gson.toJson(loginCredentials);

        // Send the login request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(new URI(loginURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonLoginCredentials))
                .build();

        HttpResponse<String> response = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        // Print the response body along with other information
        System.out.println("Response Body: " + response.body());

        // Handle the response and save the token and token type
        if (response.statusCode() == 200) {
            extractAuthToken(response.body());
            System.out.println("Login successful. AuthToken: " + authToken);
            System.out.println("TokenType: " + tokenType);
        } else {
            System.out.println("Login failed. HTTP Status Code: " + response.statusCode());
        }
    }

    private void extractAuthToken(String responseBody) {
        Gson gson = new Gson();
        AuthResponse authResponse = gson.fromJson(responseBody, AuthResponse.class);

        // Save the token and token type to the instance variables
        authToken = authResponse.getAccessToken();
        tokenType = authResponse.getTokenType();
    }


    public void register() throws URISyntaxException, IOException, InterruptedException {
        String registerURL = "http://localhost:8080/auth/register";

        // Get user inputs for username and password
        String username = Input.stringPut("Enter your username: ");
        String password = Input.stringPut("Enter your password: ");

        // Create a RegisterDTO object
        var registerCredentials = new RegisterDTO(username, password);

        // Convert the RegisterDTO to JSON
        Gson gson = new Gson();
        String jsonRegisterCredentials = gson.toJson(registerCredentials);

        // Send the register request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest registerRequest = HttpRequest.newBuilder()
                .uri(new URI(registerURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRegisterCredentials))
                .build();

        HttpResponse<String> response = client.send(registerRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        // (e.g., parse JSON response, handle registration success/failure, etc.)
        System.out.println(response.body());
    }

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
            gson.fromJson(response.body(), productsType);
        } else {
            System.out.println("Error fetching products. Status code: " + statusCode);
        }
    }

    public void showCart() throws URISyntaxException, IOException, InterruptedException {
        String cartURL = "http://localhost:8080/cart/";

        // Ensure authToken is not null or empty before making the request
        if (authToken == null || authToken.isEmpty()) {
            System.out.println("Authentication token is missing. Please log in first.");
            return;
        }

        // Send the show cart request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest showCartRequest = HttpRequest.newBuilder()
                .uri(new URI(cartURL))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken) // Include the authentication token in the request header
                .build();

        HttpResponse<String> response = client.send(showCartRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            Type cartType = new TypeToken<List<ProductDTO>>() {}.getType();
            List<ProductDTO> cartProducts = gson.fromJson(response.body(), cartType);

            // Print or process the cart products
            System.out.println("Cart Contents:");
            for (ProductDTO product : cartProducts) {
                System.out.println("Product ID: " + product.getId() + ", Name: " + product.getName() + ", Price: " + product.getPrice());
            }
        } else {
            System.out.println("Error fetching cart. Status code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        }
    }

    public void addToCart(long productId) throws URISyntaxException, IOException, InterruptedException {
        String addToCartURL = "http://localhost:8080/cart/" + productId;

        // Ensure authToken is not null or empty before making the request
        if (authToken == null || authToken.isEmpty()) {
            System.out.println("Authentication token is missing. Please log in first.");
            return;
        }

        // Send the addToCart request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest addToCartRequest = HttpRequest.newBuilder()
                .uri(new URI(addToCartURL))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken) // Include the authentication token in the request header
                .POST(HttpRequest.BodyPublishers.noBody()) // Assuming POST request without a request body
                .build();

        HttpResponse<String> response = client.send(addToCartRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the response as needed
        if (response.statusCode() == 200) {
            System.out.println("Product added to the cart successfully.");

            // Print the cart contents after adding the product
            showCart();
        } else {
            System.out.println("Error adding product to the cart. Status code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        }
    }

    public void checkout() throws URISyntaxException, IOException, InterruptedException {
        String checkoutURL = "http://localhost:8080/checkout";

        // Show cart details
        showCart();

        // Continue with the checkout logic
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest checkoutRequest = HttpRequest.newBuilder()
                .uri(new URI(checkoutURL))
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> checkoutResponse = client.send(checkoutRequest, HttpResponse.BodyHandlers.ofString());

        // Handle the checkout response as needed
        System.out.println("Checkout Response: " + checkoutResponse.body());
    }
}
