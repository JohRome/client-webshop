package com.jrome;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jrome.payload.*;
import com.jrome.utils.Input;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static com.jrome.utils.JSONConverter.dtoToJSON;
import static com.jrome.utils.JSONConverter.jsonToDto;

public class Main {
    public static void main(String[] args) {

        // TODO: Menu based program so admin and customer can make choices and take action!
        // TODO 1: Download Lombok - DONE
        // TODO 2: Make menus - One for admin and one for customer - DONE
        // TODO 3: Static Input class for user inputs - DONE
        // TODO 4: Static Output class for the frontend if needed - WAIT WITH THIS
        // TODO 5: Handler classes(?) for the payload class(es) so we can construct objects we need - WAIT WITH THIS
        // TODO 6: Place methods below in classes
        // TODO 6.1: Make methods more dynamic, not hardcoded as they are now
        // TODO 6.2: Better storage for JWT-Token is needed. Feels cunty to make a new login everytime
        // TODO: 6.3: The HttpClient.class needs to implement all of the http request methods - GET/POST/PUT/DELETE
        // TODO: 6.3.1: Each method needs to be implemented in such way that it's as dynamical as possible
        // TODO: 6.4: AdminAPI.class uses methods from the HttpClient.class and ONLY admin related requests
        // TODO: 6.5: CustomerAPI.class uses methods from the HttpClient.class and ONLY customer related requests

    }

    // TODO: Need to test methods below with webshop's branch: sandra-new

    public static String adminLogin() throws URISyntaxException, IOException, InterruptedException {
        // URL:n vi vill åt, i detta fall den vi behöver för att logga in som admin
        String adminURL = "http://localhost:8080/auth/login";

        // I Postman så måste vi ju skicka med en JSON body för username och password
        // så vi skapar en LoginDTO klass som representerar det vi ska skicka in och tilldelar alla värden
        var adminCredentials = new LoginDTO(
                "admin",
                "admin"
        );

        // Vi konverterar sedan klassen till en JSON sträng så vi slipper skriva jobbiga JSON-format själva
        Gson gson = new Gson();
        String jsonAdminCredentials = gson.toJson(adminCredentials);

        // Javas egna HttpClient bibliotek som är smidig som satan själv
        HttpClient client = HttpClient.newHttpClient();

        // Vi skapar requesten vi vill utföra
        HttpRequest logInRequest = HttpRequest.newBuilder()
                .uri(new URI(adminURL))
                // Detta är pissviktigt också för att säga åt att vi vill skicka i JSON och inte något annat skit
                .header("Content-Type", "application/json")
                // Själva requesten
                .POST(HttpRequest.BodyPublishers.ofString(jsonAdminCredentials))
                .build();

        // Efter vi skickat sparar vi det vi får tillbaka i HttpResponse response variabeln
        HttpResponse<String> response = client.send(logInRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

        // Via denna kan vi få ut en massa skön skit, bland annat status koden som är 200 i vårat fall
        int statusCode = response.statusCode();

        // Och det VIKTIGASTE, body:n. Det är här allt gottigott ligger. I detta fall våran JWT-Token response
        String body = response.body();

        // Så vi tar responsen och konverterar den från JSON till ResponseDTO denna gången med samma
        // gson objekt som vi använde oss först
        var convertedBody = gson.fromJson(body, JWTResponseDTO.class);

        // och skriver sen ut skiten i en simpel toString(), bara för skojs skull.
        System.out.println(convertedBody);

        // Vi gör det enkelt och fult och skickar med JWT-Token response som en String som kan användas i alla
        // admin requests
        return convertedBody.getTokenType() + " " + convertedBody.getAccessToken();

    }


    public static void addProductAsAdmin(String productName,
                                         double productCost,
                                         String productDesc)
            throws URISyntaxException, IOException, InterruptedException {

        // I denna variabel sparar vi JWT-Token från när admin loggar in så vi kan använda denna i varje request
        String token = adminLogin();


        // URL:n vi vill åt, i detta fall den vi behöver för att lägga till produkter som admin
        String adminURL = "http://localhost:8080/products/admin";

        // I Postman så måste vi ju skicka med en JSON body för den produkt vi vill lägga till
        // så vi skapar en ProductDTO klass som representerar det vi ska skicka in och tilldelar alla värden
        var product = new ProductDTO();
        product.setName(productName);
        product.setPrice(productCost);
        product.setDescription(productDesc);

        // Vi konverterar sedan klassen till en JSON sträng så vi slipper skriva jobbiga JSON-format själva
        Gson gson = new Gson();
        String jsonMilk = gson.toJson(product);


        // Javas egna HttpClient bibliotek som är smidig som satan själv
        HttpClient client = HttpClient.newHttpClient();

        // Vi skapar requesten vi vill utföra
        HttpRequest addProductRequest = HttpRequest.newBuilder()
                .uri(new URI(adminURL))
                // Detta är pissviktigt också för att säga åt att vi vill skicka i JSON och inte något annat skit
                .header("Content-Type", "application/json")
                // Här kommer det viktigaste. Vi måste sätta hit våran token
                .header("Authorization", token)
                // Själva requesten
                .POST(HttpRequest.BodyPublishers.ofString(jsonMilk))
                .build();

        // Efter vi skickat sparar vi det vi får tillbaka i HttpResponse response variabeln
        HttpResponse<String> response = client.send(addProductRequest, HttpResponse.BodyHandlers.ofString());

        // Via denna kan vi få ut en massa skön skit, bland annat status koden som är 200 i vårat fall
        int statusCode = response.statusCode();

        // Och det VIKTIGASTE, body:n. Det är här allt gottigott ligger. I detta fall våran JWT-Token response
        String body = response.body();

        // Så vi tar responsen och konverterar den från JSON till ResponseDTO denna gången med samma
        // gson objekt som vi använde oss först
        var convertedBody = gson.fromJson(body, ProductDTO.class);

        // Skriv ut statuskod för att se om det är OK
        System.out.println(statusCode);

        // Skriv ut ProductDTO för att se att det fungerade
        System.out.println(convertedBody);
    }

    public static void registerCustomer(String username, String password)
            throws URISyntaxException, IOException, InterruptedException {

        String registerURL = "http://localhost:8080/auth/register";

        var customerCredentials = new RegisterDTO(
                username,
                password
        );

        Gson gson = new Gson();
        String jsonCustomer = gson.toJson(customerCredentials);

        HttpClient client = HttpClient.newHttpClient();


        HttpRequest registerRequest = HttpRequest.newBuilder()
                .uri(new URI(registerURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonCustomer))
                .build();

        HttpResponse<String> response = client.send(registerRequest, HttpResponse.BodyHandlers.ofString());

        System.out.printf("Response code: %d - %s", response.statusCode(), response.body());
    }

    public static String loginCustomer(String username, String password)
            throws URISyntaxException, IOException, InterruptedException {

        String loginURL = "http://localhost:8080/auth/login";

        var customerCredentials = new LoginDTO(
                username,
                password
        );

        Gson gson = new Gson();
        String jsonCustomer = gson.toJson(customerCredentials);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(new URI(loginURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonCustomer))
                .build();
        HttpResponse<String> response = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        var convertedBody = gson.fromJson(response.body(), JWTResponseDTO.class);
        System.out.println(convertedBody);

        return convertedBody.getTokenType() + " " + convertedBody.getAccessToken();
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


        Gson gson = new Gson();

        Type products = new TypeToken<ArrayList<ProductDTO>>() {
        }.getType();

        return gson.fromJson(response.body(), products);
    }

    public static void addProductsToCart(long id, int quantity)
            throws URISyntaxException, IOException, InterruptedException {

        String addToCartURL = "http://localhost:8080/cart/" + id;
        // Fult, hårdkodat namn och lösen för skojs skull
        String token = loginCustomer("Lukas", "SpearInEyesson");

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

    public static void deleteProductFromCart(long id) throws URISyntaxException, IOException, InterruptedException {

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
}
