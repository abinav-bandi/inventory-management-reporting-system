package com.inventory.model;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class ProductsValidationTest {
    @Test
    public void testProductsValidation() throws InputMismatchException {
        Product p=new Product(1,"mouse","electronic",10,300);
        assertEquals(1,p.getId());
        assertEquals("mouse",p.getName());
    }
    @Test
    public void testInvalidPriceThrowsException() {
        Product p = new Product(1,"mouse","electronic",10,300);
        Exception ex = assertThrows(
                InputMismatchException.class,
                () -> p.setPrice(-300)
        );
        assertEquals("❌Price cannot be negitive", ex.getMessage());
    }
    @Test
    public void testInvalidQuantityThrowsException() {
        Product p = new Product(1,"mouse","electronic",10,300);
        Exception ex = assertThrows(
                InputMismatchException.class,
                () -> p.setQuantity(-5)
        );
        assertEquals("❌Quantity must be greater than 0", ex.getMessage());
    }


}
