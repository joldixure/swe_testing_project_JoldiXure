package CostumerManagment;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class manageProfiles {

    public static void showManageProfilesDialog(ObservableList<CustomerManagement.Customer> selectedCustomers) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Manage Profiles");
        dialog.setHeaderText("Manage Customer Profiles");

       
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        
        TextField nameField = new TextField();
        TextField contactDetailsField = new TextField();
        TextField purchaseHistoryField = new TextField();

        if (selectedCustomers.size() == 1) {
            CustomerManagement.Customer selectedCustomer = selectedCustomers.get(0);
            nameField.setText(selectedCustomer.getName());
            contactDetailsField.setText(selectedCustomer.getContactDetails());
            purchaseHistoryField.setText(selectedCustomer.getPurchaseHistory());
        }

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Contact Details:"), 0, 1);
        grid.add(contactDetailsField, 1, 1);
        grid.add(new Label("Purchase History:"), 0, 2);
        grid.add(purchaseHistoryField, 1, 2);

        
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

       
        nameField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(newValue.trim().isEmpty()));
        contactDetailsField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(newValue.trim().isEmpty()));
        purchaseHistoryField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(grid);

        nameField.requestFocus();

      
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(nameField.getText(), contactDetailsField.getText());
            }
            return null;
        });

       
        dialog.showAndWait().ifPresent(profileData -> {
          
            for (CustomerManagement.Customer selectedCustomer : selectedCustomers) {
                selectedCustomer.setName(profileData.getKey());
                selectedCustomer.setContactDetails(profileData.getValue());
                  }

            CustomerManagement.saveCustomersToFile();
        });
    }
}
