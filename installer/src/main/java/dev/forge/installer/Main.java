package dev.forge.installer;

import java.util.Scanner;

public class Main {
    private static Scanner scnr = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(
                """


                         _____ ___  ____   ____ _____   _ _
                        |  ___/ _ \\|  _ \\ / ___| ____| | | |
                        | |_ | | | | |_) | |  _|  _|   | | |
                        |  _|| |_| |  _ <| |_| | |___  |_|_|
                        |_|   \\___/|_| \\_\\\\____|_____| (_|_)
                        from java developers to java developers

                        forge is a plugin-based engine that aims to be all-in-one tool to help java developers
                        please report any issue, suggestion, question, or anything you want to this email rawadaboughanem0@gmail.com
                                                        """);

        System.out.println(
                """
                        welcome to forge installer
                        forge is not more than couple of fat-jars and each forge plugin is just another jar.
                        everything forge will ever use or install is placed in $HOME/.forge/ directory
                        NOTE: installer will install forge engine and forge plugins-manager plugin in order to make working with forge plugin a piece of cake
                        so that you can start using forge plugins-manager commands directly
                                        """);

        System.out.print("Proceed with installation [Y/n]:");
        String answer = scnr.nextLine().toLowerCase();
        System.out.println();
        if (!answer.equals("y")) {
            System.out.println("installatino cancelled");
            return;
        }

        try {
            ForgeInitializer.initialize();
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED: " + e.getMessage());
        }

    }
}
