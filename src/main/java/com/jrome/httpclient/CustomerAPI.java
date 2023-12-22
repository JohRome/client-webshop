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
import java.util.Collections;
import java.util.List;

import com.jrome.utils.Input;



public class CustomerAPI {


    // Auth Related



    public static void login() throws URISyntaxException, IOException, InterruptedException {
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

        // Handle the response as needed
        // (e.g., parse JSON response, handle authentication token, etc.)
        System.out.println(response.body());
    }

    public static void register() throws URISyntaxException, IOException, InterruptedException {
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




    public static List<ProductDTO> getAllProducts()
            throws URISyntaxException, IOException, InterruptedException {
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
            return gson.fromJson(response.body(), productsType);
        } else {
            System.out.println("Error fetching products. Status code: " + statusCode);
            return Collections.emptyList(); // Or handle the error accordingly
        }
    }

     // Delete product from cart
   /** public static void deleteProductFromCart(long id) throws URISyntaxException, IOException, InterruptedException {

        String removeItemFromCart = "http://localhost:8080/cart/" + id;
        // Fult, hårdkodat namn och lösen för skojs skull
       String token = loginCustomer("Lukas", "SpearInEyesson");


        HttpClient client = HttpClient.newHttpClient();


        HttpRequest addProductRequest = HttpRequest.newBuilder()
                .uri(new URI(removeItemFromCart))
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(addProductRequest, HttpResponse.BodyHandlers.ofString());
    }

    // Add product to cart
    public static void addProductsToCart(long id, int quantity)
            throws URISyntaxException, IOException, InterruptedException {

        String addToCartURL = "http://localhost:8080/cart/" + id;
        // Fult, hårdkodat namn och lösen för skojs skull
        //  String token = loginCustomer("Lukas", "SpearInEyesson");

        var quantityToCart = new ProductQuantityToCartDTO(
                quantity
        );

        Gson gson = new Gson();

        var payload = gson.toJson(quantityToCart);

        HttpClient client = HttpClient.newHttpClient();


        HttpRequest addProductRequest = HttpRequest.newBuilder()
                .uri(new URI(addToCartURL))
                // Detta är pissviktigt också för att säga åt att vi vill skicka i JSON och inte något annat skit
                .header("Content-Type", "application/json")
                // Här kommer det viktigaste. Vi måste sätta hit våran token
                .header("Authorization", token)
                // Själva requesten
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = client.send(addProductRequest, HttpResponse.BodyHandlers.ofString());


    }

    public static void showCart() {

    }
    **/
}
