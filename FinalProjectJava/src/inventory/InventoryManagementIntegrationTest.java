package inventory;

import static org.junit.jupiter.api.Assertions.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import java.io.File;
import java.lang.reflect.Field;

public class InventoryManagementIntegrationTest {

    private File inventoryFile = new File("inventory.txt");
    private File backupFile = new File("inventory_backup_test.txt");

    @BeforeEach
    void backupFile() {
        if (inventoryFile.exists()) {
            inventoryFile.renameTo(backupFile);
        }
    }

    @AfterEach
    void restoreFile() {
        if (inventoryFile.exists()) inventoryFile.delete();
        if (backupFile.exists()) backupFile.renameTo(inventoryFile);
    }

    private InventoryManagement prepareInventory() throws Exception {
        InventoryManagement im = new InventoryManagement();

        ObservableList<Product> list = FXCollections.observableArrayList();
        Field field = InventoryManagement.class.getDeclaredField("inventory");
        field.setAccessible(true);
        field.set(im, list);

        return im;
    }

    @Test
    void testSaveAndLoadInventory() throws Exception {
        InventoryManagement im = prepareInventory();

        im.getInventoryList().add(new Product(1, "Apple", "Food", 1.5, 10, "SupplierA"));
        im.getInventoryList().add(new Product(2, "Banana", "Food", 0.8, 20, "SupplierB"));

        im.saveInventoryToFile();

        InventoryManagement loaded = prepareInventory();
        loaded.loadInventoryFromFile();

        assertEquals(2, loaded.getInventoryList().size());
        assertEquals("Apple", loaded.getInventoryList().get(0).getName());
    }
}
