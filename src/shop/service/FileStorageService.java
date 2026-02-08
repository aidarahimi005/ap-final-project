package shop.service;

import shop.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {
    private static final String PRODUCTS_FILE = "products.csv";
    private static final String USERS_FILE = "users.csv";

    public FileStorageService() {
        createFileIfNotExists(PRODUCTS_FILE);
        createFileIfNotExists(USERS_FILE);
    }

    private void createFileIfNotExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                if (fileName.equals(USERS_FILE)) {
                    saveUsers(List.of(new Admin("admin", "admin")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    products.add(new Product(parts[0], parts[1], Double.parseDouble(parts[2]),
                            Integer.parseInt(parts[3]), parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void saveProducts(List<Product> products) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PRODUCTS_FILE))) {
            for (Product p : products) {
                pw.println(p.getId() + "," + p.getName() + "," + p.getPrice() + "," +
                        p.getStock() + "," + p.getCategory() + "," + (p.getImagePath() == null ? "null" : p.getImagePath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    if (parts[2].equals("ADMIN")) {
                        users.add(new Admin(parts[0], parts[1]));
                    } else if (parts[2].equals("CUSTOMER")) {
                        double balance = (parts.length > 3) ? Double.parseDouble(parts[3]) : 0.0;
                        users.add(new Customer(parts[0], parts[1], balance));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void saveUsers(List<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                String line = u.getUsername() + "," + u.getPassword() + "," + u.getRole();
                if (u instanceof Customer) {
                    line += "," + ((Customer) u).getBalance();
                }
                pw.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

