package POS;

import First.MainPage;
import Sales.SalesReportManager;
import inventory.InventoryManagement;
import inventory.Product;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PointOfSale extends Application {

    private List<Product> inventory = new ArrayList<>();
    private List<TransactionItem> transaction = new ArrayList<>();
    private InventoryManagement inventoryManagement;
    private Stage inventoryStage;
    private SalesReportManager salesReportManager;

    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Point of Sale System");
        inventoryManagement = new InventoryManagement();
        initializeInventoryManagementStage();

        initializeInventory();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Label itemLabel = new Label("Item:");
        ComboBox<Product> itemComboBox = new ComboBox<>();
        itemComboBox.getItems().addAll(inventory);

        itemComboBox.setCellFactory(param -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        itemComboBox.setButtonCell(new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        Button addItemButton = new Button("Add Item");
        Button calculateButton = new Button("Calculate Total");
        Button generateReceiptButton = new Button("Generate Receipt");
        Button backButton = new Button("Back to Main Page");

        grid.add(itemLabel, 0, 0);
        grid.add(itemComboBox, 1, 0);
        grid.add(quantityLabel, 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(addItemButton, 1, 2);
        grid.add(calculateButton, 1, 3);
        grid.add(generateReceiptButton, 1, 4);
        grid.add(backButton, 1, 7);
        

        TextArea receiptTextArea = new TextArea();
        receiptTextArea.setEditable(false);
        grid.add(receiptTextArea, 0, 5, 2, 1);

        addItemButton.setOnAction(e -> {
            Product selectedItem = itemComboBox.getValue();
            String quantity = quantityField.getText();

            if (selectedItem != null && !quantity.isEmpty()) {
                int sellQuantity = Integer.parseInt(quantity);
                if (sellQuantity <= selectedItem.getQuantityInStock()) {
                    addItemToTransaction(selectedItem, sellQuantity);
                    
                    selectedItem.setQuantityInStock(selectedItem.getQuantityInStock() - sellQuantity);
                   
                    inventoryManagement.updateTableView();
                    
                    inventoryManagement.saveInventoryToFile();
                } else {
                    showAlert("Error", "Not enough quantity in stock.");
                }
            } else {
                showAlert("Error", "Please select an item and enter quantity.");
            }
        });

        calculateButton.setOnAction(e -> calculateTotal());

        generateReceiptButton.setOnAction(e -> generateReceipt(receiptTextArea));
        backButton.setOnAction(e -> backToMainPage(primaryStage));

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public void setInventoryManagement(InventoryManagement inventoryManagement) {
        this.inventoryManagement = inventoryManagement;
    }

    private void initializeInventory() {
        inventory = inventoryManagement.getInventoryList();
    }
    private void initializeInventoryManagementStage() {
        
        inventoryStage = new Stage();
        inventoryManagement.start(inventoryStage);
    }

    private void backToMainPage(Stage primaryStage) {
        MainPage mainPage = new MainPage();
        mainPage.showMainPage(primaryStage, "admin"); 

        
 
    }


    private void calculateTotal() {
        double total = 0;
        for (TransactionItem item : transaction) {
            total += item.getTotal();
        }
        showAlert("Total", "Total Cost: $" + total);
    }

    private void generateReceipt(TextArea receiptTextArea) {
        StringBuilder receipt = new StringBuilder("Receipt:\n");

        for (TransactionItem item : transaction) {
            receipt.append(item.getItem().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" - Total: $").append(item.getTotal())
                    .append("\n");
        }

        double totalCost = calculateTotalCost();
        receipt.append("\nTotal Cost: $").append(totalCost);
        receipt.append("\nThank You!");
        receipt.append("\nED Market");

        // Display the receipt in the TextArea
        receiptTextArea.setText(receipt.toString());

        saveReceiptToFile(receipt.toString());
        showAlert("Receipt Generated", "Receipt has been generated and saved.");
        resetTransaction();
        saveSalesInformationToReport();
    }

    private double calculateTotalCost() {
        return transaction.stream().mapToDouble(TransactionItem::getTotal).sum();
    }

    private void saveReceiptToFile(String receipt) {
        File file = new File("receipt.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(receipt);
            System.out.println("Receipt saved to file: " + file.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addItemToTransaction(Product product, int quantity) {
        int availableQuantity = product.getQuantityInStock();

        if (availableQuantity >= quantity) {
            
            TransactionItem transactionItem = new TransactionItem(product, quantity);
            transaction.add(transactionItem);

            
            int newQuantityInStock = availableQuantity - quantity+1;
            product.setQuantityInStock(newQuantityInStock);

          
            for (Product inventoryProduct : inventory) {
                if (inventoryProduct.getId() == product.getId()) {
                    inventoryProduct.setQuantityInStock(newQuantityInStock);
                    break;
                }
            }

            inventoryManagement.updateTableView();

            showAlert("Success", "Item added to transaction.");
        } else {
            showAlert("Error", "Insufficient quantity in stock.");
        }
    }



    private void resetTransaction() {
        transaction.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
    public void setSalesReportManager(SalesReportManager salesReportManager2) {
        this.salesReportManager = salesReportManager2;
    }

private void saveSalesInformationToReport() {
    for (TransactionItem item : transaction) {
        salesReportManager.addSalesRecord(
                item.getItem().getName(),
                item.getQuantity(),
                item.getTotal(),
                new Date()
        );
    }
}
}