package com.jrome;

import com.jrome.httpclient.CustomerAPI;
import com.jrome.payload.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import static com.jrome.httpclient.CustomerAPI.getAllProducts;


public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        List<ProductDTO> products = getAllProducts();
        System.out.println(products); // Fungerar

        CustomerAPI.login(); // Fungerar
        CustomerAPI.register(); // Fungerar

        // CustomerAPI.addToCart(id""); // Ej implementerad
        // CustomerAPI.showCart(); // Ej implementerad
    }
}


















