package com.jrome.utils;

import java.util.Scanner;


public class Input {
    private static final Scanner scanner = new Scanner(System.in);

    public static String stringInput() {
        return scanner.nextLine().trim();
    }

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
