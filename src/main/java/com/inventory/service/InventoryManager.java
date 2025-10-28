package com.inventory.service;

import com.inventory.dao.ProductDAOImpl;
import com.inventory.dao.UserDAOImpl;
import com.inventory.exception.InvalidInputException;
import com.inventory.model.Product;
import com.inventory.exception.NoProductFoundException;
import com.inventory.exception.InvalidQuantityException;
import com.inventory.model.User;
import com.inventory.util.CSVHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryManager{
    private Scanner sc = new Scanner(System.in);
    private ProductDAOImpl dao = new ProductDAOImpl();
    private UserDAOImpl userDao = new UserDAOImpl();


    // Add product
    public void addProduct() {
        try {
            System.out.print("🆔 Enter ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("📦 Enter Name: ");
            String name = sc.nextLine();

            System.out.print("🏷️ Enter Category: ");
            String category = sc.nextLine();

            System.out.print("🔢 Enter Quantity: ");
            int qty = Integer.parseInt(sc.nextLine());
            if (qty < 0) throw new InvalidQuantityException("❌ Quantity cannot be negative");

            System.out.print("💰 Enter Price: ");
            double price = Double.parseDouble(sc.nextLine());
            if (price < 0) throw new InvalidQuantityException("❌ Price cannot be negative");

            Product p = new Product(id, name, category, qty, price);
            dao.addProduct(p);
            System.out.println("✅ Product added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid input! Please enter numbers for ID, Quantity, and Price.");
        } catch (InvalidQuantityException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Remove product
    public void removeProduct() {
        try {
            System.out.print("🗑️ Enter ID to remove: ");
            int id = Integer.parseInt(sc.nextLine());

            boolean deleted = dao.deleteProduct(id);
            if (!deleted) throw new NoProductFoundException("❌ Product with ID " + id + " not found.");

        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid input! Please enter a number.");
        } catch (NoProductFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Update product
    public void updateProduct() {
        try {
            System.out.print("✏️ Enter ID to update: ");
            int id = Integer.parseInt(sc.nextLine());

            Product existing = dao.getProductById(id);
            if (existing == null) throw new NoProductFoundException("❌ Product with ID " + id + " not found.");

            System.out.print("🆕 Enter new Name: ");
            String name = sc.nextLine();
            System.out.print("🆕 Enter new Category: ");
            String category = sc.nextLine();
            System.out.print("🆕 Enter new Quantity: ");
            int qty = Integer.parseInt(sc.nextLine());
            if (qty < 0) throw new InvalidQuantityException("❌ Quantity cannot be negative");

            System.out.print("🆕 Enter new Price: ");
            double price = Double.parseDouble(sc.nextLine());
            if (price < 0) throw new InvalidQuantityException("❌ Price cannot be negative");

            existing.setName(name);
            existing.setCategory(category);
            existing.setQuantity(qty);
            existing.setPrice(price);

            boolean updated = dao.updateProduct(existing);
            if (updated) {
                System.out.println("✅ Product updated successfully!");
            } else {
                System.out.println("⚠️ Update failed.");
            }

        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid input! Please enter numbers for ID, Quantity, and Price.");
        } catch (NoProductFoundException | InvalidQuantityException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Search product
    public void searchProduct() {
        try {
            System.out.println("\n🔍 === Search Product ===");
            System.out.println("1️⃣ Search by ID");
            System.out.println("2️⃣ Search by Name");
            System.out.println("3️⃣ Search by Category");
            System.out.print("➡️ Enter your choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("🔢 Enter ID to search: ");
                    int id = Integer.parseInt(sc.nextLine());
                    Product p = dao.getProductById(id);
                    if (p != null) {
                        p.display();
                    } else {
                        System.out.println("⚠️ Product not found!");
                    }
                    break;

                case 2:
                    System.out.print("📦 Enter product name to search: ");
                    String name = sc.nextLine();
                    List<Product> nameResults = dao.searchByName(name);
                    nameResults.forEach(Product::display);
                    break;

                case 3:
                    System.out.print("🏷️ Enter category to search: ");
                    String category = sc.nextLine();
                    List<Product> categoryResults = dao.searchByCategory(category);
                    categoryResults.forEach(Product::display);
                    break;

                default:
                    System.out.println("❌ Invalid choice! Please select 1, 2, or 3.");
            }

        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid input! Please enter a number.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Filter products by price range
    public void filterByPriceRange() {
        try {
            System.out.print("💲 Enter minimum price: ");
            double minPrice = Double.parseDouble(sc.nextLine());

            System.out.print("💲 Enter maximum price: ");
            double maxPrice = Double.parseDouble(sc.nextLine());

            if (minPrice < 0 || maxPrice < 0) {
                System.out.println("❌ Price cannot be negative.");
                return;
            }

            if (minPrice > maxPrice) {
                System.out.println("⚠️ Minimum price cannot be greater than maximum price.");
                return;
            }

            List<Product> allProducts = dao.getAllProducts();
            List<Product> filteredProducts = new ArrayList<>();

            for (Product p : allProducts) {
                if (p.getPrice() >= minPrice && p.getPrice() <= maxPrice) {
                    filteredProducts.add(p);
                }
            }

            if (filteredProducts.isEmpty()) {
                System.out.println("⚠️ No products found in this price range.");
            } else {
                System.out.println("\n📊 Products in the price range " + minPrice + " - " + maxPrice + ":");
                System.out.printf("%-5s %-15s %-15s %-10s %-10s%n", "ID", "Name", "Category", "Quantity", "Price");
                System.out.println("------------------------------------------------------");
                filteredProducts.forEach(p ->
                        System.out.printf("%-5s %-15s %-15s %-10s %-10.2f%n",
                                p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getPrice()));
            }

        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid input! Please enter valid numbers for price.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateReport() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("🆔 Enter ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("📦 Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("🏷️ Enter Category: ");
        String category = scanner.nextLine();

        System.out.print("🔢 Enter Quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("💰 Enter Price: ");
        double price = scanner.nextDouble();

        Product p = new Product(id, name, category, quantity, price);
        List<Product> list = new ArrayList<>();
        list.add(p);
        CSVHelper.generateProductReport(list,"Admin");
        System.out.println("📄 Report generated successfully!");
    }

    // Display all products
    public void displayAll() {
        try {
            List<Product> Products = dao.getAllProducts();
            if (Products.isEmpty()) {
                System.out.println("⚠️ No products found.");
                return;
            }
            System.out.printf("%-5s %-15s %-15s %-10s %-10s%n ", "ID", "Name", "Category", "Quantity", "Price");
            System.out.println("------------------------------------------------------");
            Products.forEach(p ->
                    System.out.printf("%-5s %-15s %-15s %-10s %-10s%n ",
                            p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getPrice()));
        } catch (Exception e) {
            System.out.println("❌ Error while fetching products: " + e.getMessage());
        }
    }

    public void addUser() {
        try {
            System.out.print("👤 Enter Username: ");
            String username = sc.nextLine().trim();
            if (username.isEmpty()) throw new InvalidInputException("❌ Username cannot be empty");

            System.out.print("🔐 Enter Password: ");
            String password = sc.nextLine().trim();
            if (password.isEmpty()) throw new InvalidInputException("❌ Password cannot be empty");

            System.out.print("🛡️ Enter Role: ");
            String role = sc.nextLine().trim();
            if (role.isEmpty()) throw new InvalidInputException("❌ Role cannot be empty");
            role = role.toUpperCase();

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);

            userDao.addUser(user);
            System.out.println("✅ User added successfully!");

        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getUserByUsername() {
        try {
            System.out.print("🔍 Enter username to search: ");
            String username = sc.nextLine().trim();

            if (username.isEmpty()) throw new InvalidInputException("❌ Username cannot be empty");

            User fetched = userDao.getUserByUsername(username);
            if (fetched != null) {
                System.out.println("✅ User Found: " + fetched.getUsername() + "\n🛡️ Role: " + fetched.getRole());
            } else {
                System.out.println("⚠️ User Not Found!");
            }

        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUser() {
        try {
            System.out.print("🗑️ Enter username to remove: ");
            String username = sc.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("❌ Username cannot be empty.");
                return;
            }

            User existingUser = userDao.getUserByUsername(username);
            if (existingUser == null) {
                System.out.println("⚠️ User not found: " + username);
                return;
            }

            boolean deleted = userDao.deleteUser(username);
            if (deleted) {
                System.out.println("✅ User '" + username + "' removed successfully!");
            } else {
                System.out.println("⚠️ Failed to delete user.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
