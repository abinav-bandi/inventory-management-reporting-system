package com.inventory.dao;
import com.inventory.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductDAOImplMockitoTest {
    public static ProductDao productDao;

    @Before
    public void setUp() throws Exception{
        productDao= Mockito.mock(ProductDao.class);
    }
    @Test
    public void testAddProductSuccess() throws Exception{
        Product p=new Product(1,"mouse","electronic",10,300);
        doNothing().when(productDao).addProduct(p);
        productDao.addProduct(p);
        verify(productDao,times(1)).addProduct(p);

    }
    @Test
    public void testUpdateProduct() throws Exception {
        Product p = new Product(1, "mouse", "electronic", 10, 300);
        when(productDao.updateProduct(p)).thenReturn(true);
        boolean result = productDao.updateProduct(p);
        assertTrue(result);
    }
    @Test
    public void testDeleteProduct() throws Exception {
        int productId = 1;
        when(productDao.deleteProduct(productId)).thenReturn(true); // mock return
        boolean result = productDao.deleteProduct(productId);
        assertTrue(result);
    }
    @Test
    public void testGetProductById() throws Exception {
        Product p = new Product(1, "mouse", "electronic", 10, 300);
        when(productDao.getProductById(1)).thenReturn(p); // mock return
        Product result = productDao.getProductById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("mouse", result.getName());
    }
    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> mockProducts = Arrays.asList(
                new Product(1, "mouse", "electronic", 10, 300),
                new Product(2, "keyboard", "electronic", 5, 500)
        );

        when(productDao.getAllProducts()).thenReturn(mockProducts);

        List<Product> result = productDao.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("mouse", result.get(0).getName());
        assertEquals("keyboard", result.get(1).getName());
    }
}
