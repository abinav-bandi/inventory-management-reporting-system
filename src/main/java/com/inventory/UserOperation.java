package com.inventory;

import com.inventory.dao.ProductDAOImpl;
import com.inventory.dao.ProductDao;
import com.inventory.service.InventoryManager;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserOperation {
    public static final Scanner sc = new Scanner(System.in);
    public static final ProductDao dao = new ProductDAOImpl();
    public static final InventoryManager manager = new InventoryManager();

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("\n==== INVENTORY MENU ====");
                System.out.println("1. Add user");
                System.out.println("2. get user by name");
                System.out.println("3. remove user");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        manager.addUser();
                        break;
                    case 2:
                        manager.getUserByUsername();
                        break;
                    case 3:
                        manager.removeUser();
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (InputMismatchException e) {
                System.out.println("invalid input");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("exception" + e.getMessage());
                sc.nextLine();
            }
        }
    }

}
