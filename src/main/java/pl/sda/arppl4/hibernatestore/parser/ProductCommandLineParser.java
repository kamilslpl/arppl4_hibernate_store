package pl.sda.arppl4.hibernatestore.parser;

import pl.sda.arppl4.hibernatestore.dao.ProductDao;
import pl.sda.arppl4.hibernatestore.model.Product;
import pl.sda.arppl4.hibernatestore.model.ProductUnit;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ProductCommandLineParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Scanner scanner;
    private final ProductDao dao;

    public ProductCommandLineParser(Scanner scanner, ProductDao dao) {
        this.scanner = scanner;
        this.dao = dao;
    }

    public void parse() {
        String command = null;

        do {
            System.out.println("Command: (add/list)");
            command = scanner.next();
            if (command.equalsIgnoreCase("add")) {
                handleAddCommand();
            } else if (command.equalsIgnoreCase("list")) {
                handleListCommand();
            }
        } while (!command.equals("quite"));

    }

    private void handleListCommand() {
        List<Product> productList = dao.zwrocListeProducts();
        for (Product product : productList) {
            System.out.println(product);

        }
        System.out.println();
    }

    private void handleAddCommand() {
        System.out.println("Provide name:");
        String name = scanner.next();

        System.out.println("Provide price:");
        Double price = scanner.nextDouble();

        System.out.println("Provide producent:");
        String producent = scanner.next();

        LocalDate expirydate = loadExpiryDateFromUser();

        System.out.println("Provide quantity:");
        Double quantity = scanner.nextDouble();

        ProductUnit productUnit = loadProductUnitFromUser();

        Product product = new Product(null, name, price, producent, expirydate, quantity, productUnit);
        dao.dodajProduct(product);

    }

    private ProductUnit loadProductUnitFromUser() {
        ProductUnit productUnit = null;
        do {
            try {
                System.out.println("Provide unit: (UNIT,GRAM,MILILITER)");
                String unitString = scanner.next();
                productUnit = ProductUnit.valueOf(unitString.toUpperCase());
            } catch (IllegalArgumentException iae) {
                System.err.println("Wrong unit, please provide unit from: ...");
            }
        } while (productUnit == null);
        return productUnit;
    }

    private LocalDate loadExpiryDateFromUser() {
        LocalDate expiryDate = null;
        do {
            try {
                System.out.println("Provide expire date:  yyyy-MM-dd");
                String expiryDateString = scanner.next();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                expiryDate = LocalDate.parse(expiryDateString, FORMATTER);

                LocalDate today = LocalDate.now();
                if (expiryDate.isBefore(today)) {
                    throw new IllegalArgumentException("Date is before today");
                }
            } catch (IllegalArgumentException | DateTimeException iae) {
                expiryDate = null;handleListCommand();
                System.err.println("Wrong date, plase provide date in format: yyyy-MM-dd");
            }

        } while (expiryDate == null);
        return expiryDate;
    }
}
