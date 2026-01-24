package Sales;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;

public class SalesReportManagerFileIOTest {

    private final File tempFile = new File("sales_test_load.txt");

    @AfterEach
    void cleanup() {
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    void testLoadSalesRecordsFromFile_LoadsCorrectlyFormattedData() throws IOException {
        // Create a file in the exact format expected by loadSalesRecordsFromFile
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile.toPath())) {
            writer.write("Pen,2,6.0,2024-01-15 10:00:00");
            writer.newLine();
            writer.write("Notebook,1,4.0,2024-01-15 10:05:00");
            writer.newLine();
        }

        SalesReportManager manager = new SalesReportManager();
        manager.loadSalesRecordsFromFile(tempFile.getAbsolutePath());

        assertEquals(2, manager.getSalesRecords().size());
        assertEquals("Pen", manager.getSalesRecords().get(0).getItemName());
        assertEquals(6.0, manager.getSalesRecords().get(0).getTotalCost(), 0.0001);
    }
}
