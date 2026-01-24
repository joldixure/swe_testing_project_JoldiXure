package inventory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    void testSetPriceValidValue() {
        Product product = new Product(1, "Laptop", "Electronics", 1000.0, 5, "Supplier A");
        product.setPrice(1200.50);
        assertEquals(1200.50, product.getPrice());
    }

    @Test
    void testSetPriceZero() {
        Product product = new Product(1, "Phone", "Electronics", 500.0, 3, "Supplier B");
        product.setPrice(0.0);
        assertEquals(0.0, product.getPrice());
    }

    @Test
    void testSetPriceNegative() {
        Product product = new Product(1, "Mouse", "Accessories", 50.0, 10, "Supplier C");
        product.setPrice(-10.0);
        assertEquals(-10.0, product.getPrice());
    }

    @Test
    void testProductGetters() {
        Product product = new Product(2, "Keyboard", "Accessories", 80.0, 7, "Supplier D");

        assertEquals(2, product.getId());
        assertEquals("Keyboard", product.getName());
        assertEquals("Accessories", product.getCategory());
        assertEquals(80.0, product.getPrice());
        assertEquals(7, product.getQuantityInStock());
        assertEquals("Supplier D", product.getSupplierInfo());
    }
}
