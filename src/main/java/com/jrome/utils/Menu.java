package com.jrome.utils;

public class Menu {

    public static void showMainMenu() {
        System.out.println("""
                Main menu
                1 - Admin menu
                2 - Customer menu
                3 - Exit program""");
    }

    public static void showAdminMenu() {
        System.out.println("""
                Admin Menu
                1 - Log in
                2 - Add products to inventory
                3 - Main menu""");
    }

    public static void showCustomerMenu() {
        System.out.println("""
                Customer menu
                1 - Browse products
                2 - Go shopping
                3 - Main menu""");
    }
}
