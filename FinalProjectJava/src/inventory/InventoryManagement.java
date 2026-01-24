package inventory;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;

public class InventoryManagement extends Application {

    private TableView<Product> tableView;
    private ObservableList<Product> inventory;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management");

        inventory = FXCollections.observableArrayList();

        
        tableView = createTableView();

       
        loadInventoryFromFile();
        primaryStage.setOnCloseRequest(windowEvent -> saveInventoryToFile());
        
        VBox layout = createLayout();

       
        Scene scene = new Scene(layout, 800, 600);

        primaryStage.setScene(scene);

       
    }

    private TableView<Product> createTableView() {
       
        TableView<Product> tableView = new TableView<>();
        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityInStockProperty().asObject());

        TableColumn<Product, String> supplierColumn = new TableColumn<>("Supplier Info");
        supplierColumn.setCellValueFactory(cellData -> cellData.getValue().supplierInfoProperty());

        tableView.getColumns().addAll(nameColumn, categoryColumn, priceColumn, quantityColumn, supplierColumn);

       
        tableView.setItems(inventory);

        return tableView;
    }

    private VBox createLayout() {
       
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Button addButton = new Button("Add Product");
        Button updateButton = new Button("Update Product");
        Button removeButton = new Button("Remove Product");
        Button backToMainButton = new Button("Back to Main Page");

       
        addButton.setOnAction(e -> showAddProductDialog());
        updateButton.setOnAction(e -> showUpdateProductDialog());
        removeButton.setOnAction(e -> removeSelectedProduct());
        backToMainButton.setOnAction(e -> backToMainPage());

        
        layout.getChildren().addAll(tableView, addButton, updateButton, removeButton, backToMainButton);

        return layout;
    }

    private void showAddProductDialog() {
        try {
           
            Optional<Product> result = ProductDialog.showAndWait();

           
            result.ifPresent(product -> {
                
                inventory.add(product);
                updateTableView(); 
                saveInventoryToFile();  
            });
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numeric values for Price and Quantity.");
        }
    }

    private void showUpdateProductDialog() {
      
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
          
            ProductDialog productDialog = new ProductDialog();
            ProductDialog.showAndWait().ifPresent(product -> {
              
                int index = inventory.indexOf(selectedProduct);
                inventory.set(index, product);
                updateTableView();  
                saveInventoryToFile();
            });
        } else {
            
            showAlert("No Product Selected", "Please select a product to update.");
        }
    }
    private void refreshTableView() {
        
        tableView.setItems(null);
        tableView.setItems(inventory);
    }

    private void removeSelectedProduct() {
      
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
           
            inventory.remove(selectedProduct);
            refreshTableView();  
            saveInventoryToFile();  
        } else {
           
            showAlert("No Product Selected", "Please select a product to remove.");
        }
    }

    private void backToMainPage() {
       
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public ObservableList<Product> getInventoryList() {
        return inventory;
    }

    public void updateTableView() {
        tableView.refresh();
    }

    public void loadInventoryFromFile() {
       
        try (BufferedReader reader = new BufferedReader(new FileReader("inventory.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = Product.readFromFile(new BufferedReader(new StringReader(line)));
                if (product != null) {
                    inventory.add(product);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Starting with an empty inventory.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveInventoryToFile() {
     
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory.txt"))) {
            for (Product product : inventory) {
                product.writeToFile(writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
