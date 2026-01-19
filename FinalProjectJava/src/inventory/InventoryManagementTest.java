package inventory;

import static org.junit.jupiter.api.Assertions.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import java.io.File;
import java.lang.reflect.Field;

public class InventoryManagementTest {

    private File inventoryFile;
    private File backupFile;

    @BeforeEach
    void backupExistingInventoryFile() {
        inventoryFile = new File("inventory.txt");
        backupFile = new File("inventory_backup_test.txt");

        if (inventoryFile.exists()) {
            if (backupFile.exists()) backupFile.delete();
            boolean renamed = inventoryFile.renameTo(backupFile);
            assertTrue(renamed, "Could not backup existing inventory.txt");
        }
    }

    @AfterEach
    void restoreInventoryFile() {
        if (inventoryFile.exists()) inventoryFile.delete();
        if (backupFile.exists()) {
            // restore original
            backupFile.renameTo(inventoryFile);
        }
    }

    private InventoryManagement createInventoryManagementWithInventoryList() throws Exception {
        InventoryManagement im = new InventoryManagement();

        ObservableList<Product> list = FXCollections.observableArrayList();

        Field invField = InventoryManagement.class.getDeclaredField("inventory");
        invField.setAccessible(true);
        invField.set(im, list);

        return im;
    }

    @Test
    void testSaveAndLoadInventoryFromFile() throws Exception {
        InventoryManagement im = createInventoryManagementWithInventoryList();

        // Add products directly into inventory list
        im.getInventoryList().add(new Product(1, "Apple", "Food", 1.5, 10, "SupplierA"));
        im.getInventoryList().add(new Product(2, "Banana", "Food", 0.8, 20, "SupplierB"));

        assertDoesNotThrow(im::saveInventoryToFile);

        // New instance loads from file
        InventoryManagement loaded = createInventoryManagementWithInventoryList();
        assertDoesNotThrow(loaded::loadInventoryFromFile);

        assertEquals(2, loaded.getInventoryList().size());
        assertEquals("Apple", loaded.getInventoryList().get(0).getName());
    }
}
