package com.jrome.utils;

import java.util.Scanner;

public class Input {

    private static final Scanner scanner = new Scanner(System.in);

    public static int intPut(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid integer.");
            System.out.print(message);
            scanner.next(); // consume the invalid input
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // consume the newline character
        return input;
    }

    public static String stringPut(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static double doublePut(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid integer.");
            System.out.print(message);
            scanner.next(); // consume the invalid input
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // consume the newline character
        return input;
    }
}
