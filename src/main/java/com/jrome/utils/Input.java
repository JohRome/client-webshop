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



    /**
     * Forces the user for the right integer input.
     * indexBeginningAt = What index is the menu starting at?
     * indexEndingAt = What index is the menu ending at?
     */
    public static int menuIntInput(int indexBeginningAt, int indexEndingAt) {

        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < indexBeginningAt || choice > indexEndingAt)
                    System.out.print("Fail. Try again: ");
                else
                    return choice;
            } catch (Exception ignored) {}
        }
    }
}
