package Sales;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class SalesReportIntegrationTest {

    @Test
    void testSalesRecordIntegrationWithSalesReportManager() {
        // Arrange
        SalesReportManager manager = new SalesReportManager();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        Date saleDate = calendar.getTime();

        // Act
        manager.addSalesRecord("Pen", 2, 10.0, saleDate);
        manager.addSalesRecord("Notebook", 1, 4.0, saleDate);

        double weeklyTotal = manager.calculateTotalSalesForWeek();

        // Assert
        assertEquals(14.0, weeklyTotal, 0.0001);
    }
}
