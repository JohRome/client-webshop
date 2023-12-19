package com.jrome;

import com.google.gson.Gson;
import com.jrome.payload.LoginDTO;
import com.jrome.payload.JWTResponseDTO;
import com.jrome.payload.ProductDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {


        // TODO: Be able to log in as admin and save JWT token to a variable - DONE
        // TODO: Be able to add Milk and Bread to the database, providing JWT - DONE

        // Lägg till vilken produkt du än vill
        addProductAsAdmin(
                "Spartan",
                300.00,
                ""
        );



    }

    /**
     * POST-request
     *
     * Loggar in som admin och skickar tillbaka ett String värde innehållandes --> "Bearer " + JWT-Token
     */
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


    /**
     * POST-request
     *
     * Loggar in som admin, låter dig bestämma produktnamn, pris och description på önskad produkt
     * för att sedan skicka in fanskapet i databasen.
     */
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
        HttpRequest addProduct = HttpRequest.newBuilder()
                .uri(new URI(adminURL))
                // Detta är pissviktigt också för att säga åt att vi vill skicka i JSON och inte något annat skit
                .header("Content-Type", "application/json")
                // Här kommer det viktigaste. Vi måste sätta hit våran token
                .header("Authorization", token)
                // Själva requesten
                .POST(HttpRequest.BodyPublishers.ofString(jsonMilk))
                .build();

        // Efter vi skickat sparar vi det vi får tillbaka i HttpResponse response variabeln
        HttpResponse<String> response = client.send(addProduct, java.net.http.HttpResponse.BodyHandlers.ofString());

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
}