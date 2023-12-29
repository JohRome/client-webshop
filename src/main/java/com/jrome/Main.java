package com.jrome;

import com.jrome.httpclient.AdminAPI;
import com.jrome.httpclient.CustomerAPI;
import com.jrome.utils.Input;

import java.io.IOException;
import java.net.URISyntaxException;



public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        CustomerAPI customerAPI = new CustomerAPI();
        AdminAPI adminAPI = new AdminAPI();

        customerAPI.login();

        customerAPI.getAllProducts();
        customerAPI.addProductAsAdmin("banan", 2, "melon kiwi citron");
        customerAPI.getAllProducts();

        // CustomerAPI.addToCart(id""); // IMPLEMENTED, since we have errors fetching cart and using it, there are still issues. Backend-problem?
        // CustomerAPI.showCart(); // IMPLEMENTED, error fetching cart. Backend-problem?
        // CustomerAPI.login(); // Implemented, now saves the authtoken throughout the program.
        // CustomerAPI.addProductAsAdmin() // IMPLEMENTED
    }
}


















