package com.inventory;

import com.inventory.dao.ProductDAOImpl;
import com.inventory.dao.ProductDao;
import com.inventory.model.User;
import com.inventory.service.EmailService;
import com.inventory.service.InventoryManager;
import com.inventory.service.StockAlertService;
import com.inventory.service.UserService;
import com.inventory.util.CSVHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final Scanner sc = new Scanner(System.in);
    public static final ProductDao dao = new ProductDAOImpl();
    public static final InventoryManager manager = new InventoryManager();

    public static void main(String[] args) {
        try {
            UserService userService = new UserService();
            Scanner scanner = new Scanner(System.in);

            User loggedInUser = null;
            boolean loggedIn = false;

            // ===== MAIN MENU LOOP =====
            while (!loggedIn) {
                int choice = -1;
                System.out.println("\n==== Welcome to Inventory Management ====");
                System.out.println("1. Register 🎉");
                System.out.println("2. Login 🔑");
                System.out.println("3. Verify Email 📧");

                // Input validation
                while (true) {
                    try {
                        System.out.print("Enter your choice: ");
                        choice = Integer.parseInt(scanner.nextLine().trim());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid input! Please enter a number.");
                    }
                }

                try {
                    switch (choice) {
                        case 1 -> {
                            // ===== Registration =====
                            System.out.print("📧 Enter new username: ");
                            String newUsername = scanner.nextLine().trim();

                            System.out.print("🔑 Enter new password: ");
                            String newPassword = scanner.nextLine().trim();

                            String role = "";
                            while (true) {
                                System.out.print("👤 Enter role (Admin/User): ");
                                role = scanner.nextLine().trim().toUpperCase();
                                if (role.equals("ADMIN") || role.equals("USER")) break;
                                System.out.println("❌ Invalid role! Enter 'Admin' or 'User'.");
                            }

                            String email = "";
                            while (true) {
                                System.out.print("Enter Email Address: ");
                                email = scanner.nextLine().trim();
                                if (email.contains("@") && email.contains(".")) break;
                                System.out.println("❌ Invalid email format! Try again.");
                            }

                            boolean registered = userService.register(newUsername, newPassword, role, email);
                            if (!registered) {
                                System.out.println("❌ Registration failed. Try again.");
                            } else {
                                System.out.println("🎉 You can now login.");
                            }
                        }
                        case 2 -> {
                            // ===== Login =====
                            System.out.print("Enter username: ");
                            String username = scanner.nextLine().trim();

                            System.out.print("Enter password: ");
                            String password = scanner.nextLine().trim();

                            loggedInUser = userService.login(username, password);
                            if (loggedInUser == null) {
                                System.out.println("❌ Invalid credentials or user not verified.");
                            } else {
                                loggedIn = true; // login successful
                            }
                        }
                        case 3 -> {
                            // ===== Verify Email =====
                            System.out.print("Enter your username to verify: ");
                            String emailToVerify = scanner.nextLine().trim();
                            boolean verified = userService.verifyEmail(emailToVerify);
                        }
                        default -> System.out.println("❌ Invalid choice! Please choose 1, 2, or 3.");
                    }
                } catch (RuntimeException e) {
                    System.out.println("⚠ Unexpected error: " + e.getMessage());
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("⚠ Database error: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // ===== LOGIN SUCCESS FLOW =====
            System.out.println("✅ Login successful! Welcome, " + loggedInUser.getUsername() +
                    " (" + loggedInUser.getRole() + ")");

            if (loggedInUser.getRole().equalsIgnoreCase("ADMIN")) {
                StockAlertService alertService = new StockAlertService();
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

                scheduler.scheduleAtFixedRate(() -> {
                    try {
                        alertService.checkStockAlerts();
                    } catch (Exception e) {
                        System.out.println("⚠ Error in stock alert: " + e.getMessage());
                    }
                }, 0, 15, TimeUnit.MINUTES);

                try {
                    adminMenu(scanner);
                } finally {
                    scheduler.shutdownNow();
                }
            } else {
                userMenu(scanner);
            }

        } catch (RuntimeException e) {
            System.out.println("⚠ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("⚠ Database or IO error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== ADMIN MENU =====
    public static void adminMenu(Scanner scanner) {
        while (true) {
            try {
                System.out.println("\n==== ADMIN INVENTORY MENU ====");
                System.out.println("1. Add product ➕");
                System.out.println("2. Remove Product ❌");
                System.out.println("3. Update product ✏️");
                System.out.println("4. Search product 🔍");
                System.out.println("5. Display All Products 📋");
                System.out.println("6. Generate Report 📝");
                System.out.println("7. Filter price by range 💰");
                System.out.println("8. Logout 👋");

                int choice;
                while (true) {
                    try {
                        System.out.print("Enter choice: ");
                        choice = Integer.parseInt(scanner.nextLine().trim());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid input! Please enter a number.");
                    }
                }

                switch (choice) {
                    case 1 -> manager.addProduct();
                    case 2 -> manager.removeProduct();
                    case 3 -> manager.updateProduct();
                    case 4 -> manager.searchProduct();
                    case 5 -> manager.displayAll();
                    case 6 -> {
                        var products = dao.getAllProducts();
                        String filePath = CSVHelper.generateProductReport(products, "Admin");
                        EmailService.sendReport(
                                "admin@company.com",
                                "Daily Inventory Report",
                                "Attached is your latest inventory report",
                                filePath
                        );
                    }
                    case 7 -> manager.filterByPriceRange();
                    case 8 -> {
                        System.out.println("👋 Logged out successfully!");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice! Try again.");
                }
            } catch (RuntimeException e) {
                System.out.println("⚠ Error in admin menu: " + e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // ===== USER MENU =====
    private static void userMenu(Scanner scanner) {
        while (true) {
            try {
                System.out.println("\n==== USER INVENTORY MENU ====");
                System.out.println("1. View all products 📋");
                System.out.println("2. Search product by ID 🔍");
                System.out.println("3. Filter by price range 💰");
                System.out.println("4. Logout 👋");

                int choice;
                while (true) {
                    try {
                        System.out.print("Enter choice: ");
                        choice = Integer.parseInt(scanner.nextLine().trim());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid input! Please enter a number.");
                    }
                }

                switch (choice) {
                    case 1 -> manager.displayAll();
                    case 2 -> manager.searchProduct();
                    case 3 -> manager.filterByPriceRange();
                    case 4 -> {
                        System.out.println("👋 Logged out successfully!");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice! Try again.");
                }
            } catch (RuntimeException e) {
                System.out.println("⚠ Error in user menu: " + e.getMessage());
            }
        }
    }
}

