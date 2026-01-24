package POS;

import static org.junit.jupiter.api.Assertions.*;

import Sales.SalesReportManager;
import inventory.Product;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PointOfSaleSalesReportIntegrationTest {

    @Test
    void testSaveSalesInformationToReport_Integration() throws Exception {
        // Arrange
        SalesReportManager reportManager = new SalesReportManager();
        PointOfSale pos = new PointOfSale();
        pos.setSalesReportManager(reportManager);

        // Create products + transaction items
        Product pen = new Product(1, "Pen", "Stationery", 5.0, 10, "SupplierA");
        Product notebook = new Product(2, "Notebook", "Stationery", 4.0, 10, "SupplierB");

        List<TransactionItem> tx = new ArrayList<>();
        tx.add(new TransactionItem(pen, 2));      // total 10
        tx.add(new TransactionItem(notebook, 1)); // total 4

        // Inject transaction list into private field: transaction
        Field transactionField = PointOfSale.class.getDeclaredField("transaction");
        transactionField.setAccessible(true);
        transactionField.set(pos, tx);

        // Act: call private method saveSalesInformationToReport()
        Method saveMethod = PointOfSale.class.getDeclaredMethod("saveSalesInformationToReport");
        saveMethod.setAccessible(true);
        saveMethod.invoke(pos);

        // Assert: SalesReportManager should now have 2 records
        assertEquals(2, reportManager.getSalesRecords().size());

        // And weekly total should include them (ensure dates fall in current week range)
        // NOTE: SalesReportManager checks: saleDate.after(startOfWeek) && saleDate.before(now)
        // PointOfSale uses new Date() so it's always within the current range.
        double weeklyTotal = reportManager.calculateTotalSalesForWeek();
        assertEquals(14.0, weeklyTotal, 0.0001);
    }
}
