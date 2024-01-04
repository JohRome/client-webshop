package com.jrome;


import com.jrome.utils.Menu;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {



    public static void main(String[] args) {
        try {


             Menu.mainMenu();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}



        // CustomerAPI.addToCart(id""); // IMPLEMENTED, since we have errors fetching cart and using it, there are still issues. Backend-problem?

        // CustomerAPI.showCart(); // IMPLEMENTED, error fetching cart. Backend-problem?

        // CustomerAPI.login(); // Implemented, now saves the authtoken throughout the program.
        // CustomerAPI.addProductAsAdmin() // IMPLEMENTED



















