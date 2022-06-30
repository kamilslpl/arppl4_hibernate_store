package pl.sda.arppl4.hibernatestore.parser;

import pl.sda.arppl4.hibernatestore.dao.ProductDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ProductCommandLineParser {
    private final Scanner scanner;
    private final ProductDao dao;

    public ProductCommandLineParser(Scanner scanner, ProductDao dao) {
        this.scanner = scanner;
        this.dao = dao;
    }

    public void parser() {
        String command = null;

        do {
            System.out.println("Command: ");
            command = scanner.next();
            if (command.equalsIgnoreCase("add")) {
                handleAddCommand();
            }

        } while (!command.equals("quite"));

    }

    private void handleAddCommand() {
        System.out.println("Provide name:");
        String name = scanner.next();

        System.out.println("Provide price:");
        String price = scanner.next();

        System.out.println("Provide producent:");
        String producent = scanner.next();

        LocalDate expiryDate = null;
        do {
            try {
                System.out.println("Provide expire date:");
                String expiryDateString = scanner.next();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                expiryDate = LocalDate.parse(expiryDateString, formatter);
            } catch (IllegalArgumentException iae) {
            System.err.println("Wrong date, plase provide date in format: yyyy-MM-dd");
            }

        }while (expiryDate == null);
    }
}
