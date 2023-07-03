package ua.prom.roboticsdmc.view;

import java.util.Scanner;

public class ViewProvider {

    private final Scanner scanner;

    public ViewProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printMessage(String message, int rowOffset, int rowLimit) {
        System.out.printf(message + " (%d - %d): \n", rowOffset + 1, rowOffset + rowLimit);
    }

    public String read() {
        return scanner.nextLine();
    }

    public int readInt() {
        return Integer.parseInt(scanner.nextLine());

    }
}
