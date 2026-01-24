package Sales;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class SalesRecordTest {

    @Test
    void testSalesRecordConstructorAndGetters() {
        Date d = new Date();
        SalesRecord r = new SalesRecord("Pen", 2, 6.0, d);

        assertEquals("Pen", r.getItemName());
        assertEquals(2, r.getQuantity());
        assertEquals(6.0, r.getTotalCost(), 0.0001);
        assertEquals(d, r.getSaleDate());
    }
}
