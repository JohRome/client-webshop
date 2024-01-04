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






















