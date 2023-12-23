package com.jrome;

import com.jrome.httpclient.CustomerAPI;
import com.jrome.utils.Input;

import java.io.IOException;
import java.net.URISyntaxException;



public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        CustomerAPI customerAPI = new CustomerAPI();
            customerAPI.login();
            customerAPI.addToCart(Input.menuIntInput("Enter item ID to add to cart"));



        // CustomerAPI.addToCart(id""); // IMPLEMENTED, since we have errors fetching cart and using it, there are still issues. Backend-problem?
        // CustomerAPI.showCart(); // IMPLEMENTED, error fetching cart. Backend-problem?
        // CustomerAPI.login(); // Implemented, now saves the authtoken throughout the program.
    }
}


















