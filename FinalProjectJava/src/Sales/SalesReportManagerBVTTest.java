package Sales;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class SalesReportManagerBVTTest {

    private SalesReportManager manager;

    @BeforeEach
    void setUp() {
        manager = new SalesReportManager();
    }

    private Date dateInsideWeek() {
        return new Date(System.currentTimeMillis() - 60 * 60 * 1000);
    }

    private Date dateBeforeWeek() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -10);
        return cal.getTime();
    }

    // BVT-01: Empty list
    @Test
    void testBVT_EmptySalesList() {
        double total = manager.calculateTotalSalesForWeek();
        assertEquals(0.0, total);
    }

    // BVT-02: Cost = 0 (invalid lower boundary)
    @Test
    void testBVT_CostZero() {
        manager.addSalesRecord("ItemA", 1, 0.0, dateInsideWeek());
        double total = manager.calculateTotalSalesForWeek();
        assertEquals(0.0, total);
    }

    // BVT-05: Cost = MAX
    @Test
    void testBVT_CostAtMaximum() {
        manager.addSalesRecord("ItemA", 1, 10_000.0, dateInsideWeek());
        double total = manager.calculateTotalSalesForWeek();
        assertEquals(10_000.0, total);
    }

    // BVT-06: Cost > MAX (invalid upper boundary)
    @Test
    void testBVT_CostAboveMaximum() {
        manager.addSalesRecord("ItemA", 1, 10_001.0, dateInsideWeek());
        double total = manager.calculateTotalSalesForWeek();
        assertEquals(0.0, total);
    }

    // BVT-08: Accumulated total exceeds MAX
    @Test
    void testBVT_TotalExceedsMaximum() {
        manager.addSalesRecord("ItemA", 1, 7_000.0, dateInsideWeek());
        manager.addSalesRecord("ItemB", 1, 5_000.0, dateInsideWeek());

        double total = manager.calculateTotalSalesForWeek();
        assertEquals(10_000.0, total);
    }

    // BVT-09: Sale outside week (invalid date)
    @Test
    void testBVT_SaleOutsideWeek() {
        manager.addSalesRecord("ItemA", 1, 5_000.0, dateBeforeWeek());
        double total = manager.calculateTotalSalesForWeek();
        assertEquals(0.0, total);
    }
}
