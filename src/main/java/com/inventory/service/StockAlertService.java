package com.inventory.service;

import com.inventory.dao.ProductDAOImpl;
import com.inventory.dao.ProductDao;
import com.inventory.model.Product;

import java.util.List;

public class StockAlertService {
    private final ProductDao dao = new ProductDAOImpl();

    public void checkStockAlerts() {
        try {
            List<Product> products = dao.getAllProducts();
            int targetStockValue = 50; // default reorder target

            for (Product p : products) {
                int threshold = p.getThreshold() > 0 ? p.getThreshold() : 10;
                if (p.getQuantity() < threshold) {
                    int reorderQty = targetStockValue - p.getQuantity();

                    // Console logs for admin/debug
                    System.out.println("=========================================");
                    System.out.println("📢 Low Stock Alert: " + p.getName());
                    System.out.println("Current Quantity: " + p.getQuantity());
                    System.out.println("Threshold: " + threshold);
                    System.out.println("Recommended Reorder Quantity: " + reorderQty);
                    System.out.println("=========================================");

                    String msg = "📢 Low Stock Alert 📢 \n"
                            + "+---------------------------+-------------------+\n"
                            + "| Product Name              | " + p.getName() + "\n"
                            + "| Current Quantity          | " + p.getQuantity() + "\n"
                            + "| Threshold                 | " + threshold + "\n"
                            + "| Recommended Reorder Qty   | " + reorderQty + "\n"
                            + "+---------------------------+-------------------+";


                    // Send alert email
                    try {
                        EmailService.sendAlert("abinavbandi16@gmail.com",
                                "Low Stock Alert: " + p.getName(), msg);
                    } catch (Exception e) {
                        System.out.println("⚠ Failed to send email alert for " + p.getName() +
                                ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("⚠ Error checking stock alerts: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
