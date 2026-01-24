package Sales;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class SalesReportManagerTest {

    private SalesReportManager manager;

    @BeforeEach
    void setUp() {
        manager = new SalesReportManager();
    }

    // Helper: compute start of current week using SAME logic as production
    private Date startOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // Helper: compute start of current month using SAME logic as production
    private Date startOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Test
    void testAddSalesRecord_AddsToInternalList() {
        Date date = new Date();
        manager.addSalesRecord("Pen", 2, 6.0, date);

        assertEquals(1, manager.getSalesRecords().size());
        assertEquals("Pen", manager.getSalesRecords().get(0).getItemName());
        assertEquals(2, manager.getSalesRecords().get(0).getQuantity());
        assertEquals(6.0, manager.getSalesRecords().get(0).getTotalCost());
    }

   

    @Test
    void testCalculateTotalSalesForWeek_BoundaryStartIsExcluded() {
        Date start = startOfWeek();

        // Exactly equal to startOfWeek should be excluded because code uses .after(startDate)
        manager.addSalesRecord("Boundary", 1, 50.0, start);

        double total = manager.calculateTotalSalesForWeek();

        assertEquals(0.0, total, 0.0001);
    }

    @Test
    void testCalculateTotalSalesForMonth_IncludesOnlyThisMonth() {
        Date start = startOfMonth();

        // inside month (1 day after start)
        Date insideMonth = new Date(start.getTime() + 24L * 60 * 60 * 1000);

        // outside month (1 day before start)
        Date beforeMonth = new Date(start.getTime() - 24L * 60 * 60 * 1000);

        Date nearNow = new Date(System.currentTimeMillis() - 2000);

        manager.addSalesRecord("A", 1, 20.0, insideMonth);
        manager.addSalesRecord("B", 1, 100.0, beforeMonth); // should NOT count
        manager.addSalesRecord("C", 1, 7.0, nearNow);

        double total = manager.calculateTotalSalesForMonth();

        assertEquals(27.0, total, 0.0001);
    }
    @Test
    void testCalculateTotalSalesForMonth_EmptyRecords() {
        double total = manager.calculateTotalSalesForMonth();
        assertEquals(0.0, total, 0.0001);
    }
}
