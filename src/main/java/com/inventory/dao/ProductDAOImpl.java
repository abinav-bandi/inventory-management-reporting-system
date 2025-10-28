package com.inventory.dao;

import com.inventory.util.DBConnection;
import com.inventory.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDao {
    public final Connection conn = DBConnection.getConnection();


    // Insert Product
    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (id, name, category, quantity, price) VALUES (?, ?, ?, ?, ?)";
             PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setInt(4, product.getQuantity());
            stmt.setDouble(5, product.getPrice());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Product added to database successfully: " + product.getName());
            } else {
                System.out.println("⚠️ Failed to add product: " + product.getName());
            }
    }
    // Get all products
    public List<Product> getAllProducts() throws SQLException {
        String sql = "SELECT * FROM products";
        List<Product> Products = new ArrayList<>();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                Products.add(p);
            }
        return Products;
    }

    // Find product by ID
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
             PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("🔍 Product found with ID: " + id);
                return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
            }else {
                System.out.println("❌ No product found with ID: " + id);
            }
        return null;
    }
    public List<Product> searchByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM products WHERE category LIKE ?";
        List<Product> products = new ArrayList<>();
             PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, "%" + category + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                products.add(p);
            }
            if (products.isEmpty()) {
                System.out.println("❌ No products found in category: " + category);
            } else {
                System.out.println("🔎 Found " + products.size() + " products in category: " + category);
            }
        return products;
    }


    // Update product
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name=?, category=?, quantity=?, price=? WHERE id=?";
             PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Product updated successfully: " + product.getName());
                return true;
            } else {
                System.out.println("⚠️ Failed to update product with ID: " + product.getId());
                return false;
            }
    }

    // Delete product
    public boolean deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
             PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("🗑️ Product deleted successfully with ID: " + id);
                return true;
            } else {
                System.out.println("❌ No product found to delete with ID: " + id);
                return false;
            }
    }

    // Search by name
    public List<Product> searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        List<Product> Products = new ArrayList<>();
             PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                Products.add(p);
            }

            if (!Products.isEmpty()) {
                System.out.println("🔎 Found " + Products.size() + " products matching: " + name);
            }
            else {
                System.out.println("❌ No products found with name like: " + name);
            }

        return Products;
    }
}
