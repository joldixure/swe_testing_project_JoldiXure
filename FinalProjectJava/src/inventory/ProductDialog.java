package inventory;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class ProductDialog {

    public static Optional<Product> showAndWait() {
       
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);

        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 20, 20));

        
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label categoryLabel = new Label("Category:");
        TextField categoryField = new TextField();

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();

        Label quantityLabel = new Label("Quantity in Stock:");
        TextField quantityField = new TextField();

        Label supplierLabel = new Label("Supplier Info:");
        TextField supplierField = new TextField();

        
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(categoryLabel, 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(priceLabel, 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(quantityLabel, 0, 3);
        grid.add(quantityField, 1, 3);
        grid.add(supplierLabel, 0, 4);
        grid.add(supplierField, 1, 4);

       
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        
        grid.add(saveButton, 0, 5);
        grid.add(cancelButton, 1, 5);

        
        saveButton.setOnAction(e -> {
           
            if (isValidInput(nameField.getText(), categoryField.getText(), priceField.getText(), quantityField.getText())) {
               
                Product newProduct = new Product(
                        0,
                        nameField.getText(),
                        categoryField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(quantityField.getText()),
                        supplierField.getText()
                );

                
                dialogStage.close();

              
                ((Stage) saveButton.getScene().getWindow()).setUserData(newProduct);
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        
        Scene scene = new Scene(grid, 400, 300);
        dialogStage.setScene(scene);

       
        dialogStage.showAndWait();

        
        return Optional.ofNullable((Product) ((Stage) saveButton.getScene().getWindow()).getUserData());
    }

    private static boolean isValidInput(String name, String category, String price, String quantity) {
        if (name.isEmpty() || category.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            showAlert("Invalid Input", "Please fill in all fields.");
            return false;
        }

        
        if (containsNumeric(name)) {
            showAlert("Invalid Input", "Name should not contain numeric values.");
            return false;
        }

        try {
            Double.parseDouble(price);
            Integer.parseInt(quantity);
            return true;
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numeric values for Price and Quantity.");
            return false;
        }
    }

    private static boolean containsNumeric(String str) {
        
        return str.matches(".*\\d.*");
    }

    private static void showAlert(String title, String content) {
       
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
