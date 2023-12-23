package com.jrome.utils;

import java.util.Scanner;


public class Input {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * At the moment this method does not validate a shit... But for testing the program out, it's fine
     */

    public static String stringPut(String output) {
        System.out.println(output);
        return scanner.nextLine();
    }




    public static int menuIntInput(String output) {
        System.out.println(output);
        return scanner.nextInt();

    }
}
