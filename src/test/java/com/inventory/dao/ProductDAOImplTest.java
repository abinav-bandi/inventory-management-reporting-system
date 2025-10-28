//package com.inventory.dao;
//
//import com.inventory.model.Product;
//import com.inventory.util.DBConnection;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class ProductDAOImplTest {
//    public static ProductDao dao;
//    @BeforeAll
//    static void setUp() throws Exception{
//        DBConnection.enableTestMode();
//        dao=new ProductDAOImpl();
//    }
//    @Test
//    public void testAddProduct() throws Exception{
//        Product p=new Product(21,"mouse","electronic",10,300);
//        dao.addProduct(p);
//        Product products=dao.getProductById(21);
//        assertEquals(21,products.getId());
//    }
//    @Test
//    public void testRemoveProduct() throws Exception {
//        // First add a product
//        Product p = new Product(22, "keyboard", "electronic", 5, 500);
//        dao.addProduct(p);
//
//        // Remove the product
//        dao.deleteProduct(22);
//
//        // Try to fetch again
//        Product product = dao.getProductById(22);
//        assertNull(product);
//    }
//
//    @Test
//    public void testGetProductById() throws Exception {
//        // Add a product
//        Product p = new Product(23, "monitor", "electronic", 2, 8000);
//        dao.addProduct(p);
//
//        // Fetch by ID
//        Product product = dao.getProductById(23);
//
//        // Check correctness
//        assertNotNull(product);
//        assertEquals(23, product.getId());
//        assertEquals("monitor", product.getName());
//        assertEquals("electronic", product.getCategory());
//        assertEquals(2, product.getQuantity());
//        assertEquals(8000, product.getPrice(),0.001);
//    }
//
//    @Test
//    public void testUpdateProduct() throws Exception {
//        // Add a product
//        Product p = new Product(24, "printer", "electronic", 3, 6000);
//        dao.addProduct(p);
//
//        // Update the product
//        p.setName("laser printer");
//        p.setQuantity(4);
//        p.setPrice(6500);
//        dao.updateProduct(p);
//
//        // Fetch again
//        Product updated = dao.getProductById(24);
//
//        // Verify updates
//        assertNotNull(updated);
//        assertEquals("laser printer", updated.getName());
//        assertEquals(4, updated.getQuantity());
//        assertEquals(6500, updated.getPrice(),0.001);
//    }
//
//}
