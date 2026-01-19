package CostumerManagment;

import First.MainPage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerManagement extends Application {

    private static List<Customer> customers = new ArrayList<>();
    private TableView<Customer> tableView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Management System");

        
        loadCustomersFromFile();

       
        VBox layout = createCustomerManagementLayout(primaryStage);

     
        Scene scene = new Scene(layout, 800, 400);

       
        primaryStage.setScene(scene);

        
        primaryStage.show();
    }

    public VBox createCustomerManagementLayout(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(20));

      
        Label titleLabel = new Label("Customer Management System");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

       
        tableView = createTableView();

        
        HBox buttonsBox = createButtonsBox(primaryStage);

     
        layout.getChildren().addAll(titleLabel, tableView, buttonsBox);

        return layout;
    }

    private TableView<Customer> createTableView() {
       
        TableView<Customer> tableView = new TableView<>();
        tableView.setEditable(false);

    
        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> contactDetailsColumn = new TableColumn<>("Contact Details");
        contactDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("contactDetails"));

        TableColumn<Customer, String> purchaseHistoryColumn = new TableColumn<>("Purchase History");
        purchaseHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseHistory"));

      
        TableColumn<Customer, Void> editButtonColumn = new TableColumn<>("Edit");
        editButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    editCustomer(customer);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

      
        TableColumn<Customer, Void> removeButtonColumn = new TableColumn<>("Remove");
        removeButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    removeCustomer(customer);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });

        
        tableView.getColumns().addAll(nameColumn, contactDetailsColumn, purchaseHistoryColumn, editButtonColumn, removeButtonColumn);

       
        ObservableList<Customer> data = FXCollections.observableArrayList(customers);
        tableView.setItems(data);

        return tableView;
    }

    private void editCustomer(Customer customer) {
        TextInputDialog editDialog = new TextInputDialog(customer.getName());
        editDialog.setTitle("Edit Customer");
        editDialog.setHeaderText("Edit customer name:");
        editDialog.setContentText("Name:");

        Optional<String> result = editDialog.showAndWait();
        result.ifPresent(newName -> {
            
            customer.setName(newName);

            
            refreshTableView();


            saveCustomersToFile();
        });
    }


    private void removeCustomer(Customer customer) {
      
        customers.remove(customer);

        refreshTableView();

        
        saveCustomersToFile();
    }



    private HBox createButtonsBox(Stage primaryStage) {
     
        Button viewCustomersButton = new Button("View Customers");
        viewCustomersButton.setOnAction(e -> viewCustomers());

        Button manageProfilesButton = new Button("Manage Profiles");
        manageProfilesButton.setOnAction(e -> manageProfiles());

        Button trackLoyaltyPointsButton = new Button("Track Loyalty Points");
        trackLoyaltyPointsButton.setOnAction(e -> trackLoyaltyPoints());

        Button applyDiscountsButton = new Button("Apply Discounts");
        applyDiscountsButton.setOnAction(e -> applyDiscounts());

        Button addCustomerButton = new Button("Add New Customer");
        addCustomerButton.setOnAction(e -> addNewCustomer());

        Button backButton = new Button("Back to Main Page");
        backButton.setOnAction(e -> backToMainPage(primaryStage));

       
        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(viewCustomersButton, manageProfilesButton, trackLoyaltyPointsButton, applyDiscountsButton, addCustomerButton, backButton);
        buttonsBox.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));

        return buttonsBox;
    }

    private void backToMainPage(Stage primaryStage) {
        MainPage mainPage = new MainPage();
        mainPage.showMainPage(primaryStage, "admin"); 
        
        
    }

    private void viewCustomers() {
       
        CustomerDialog.showCustomers(customers);
    }

    private void manageProfiles() {
       
        manageProfiles.showManageProfilesDialog(tableView.getSelectionModel().getSelectedItems());
    }


    private void trackLoyaltyPoints() {
       
        System.out.println("We didnt have time");
    }

    private void applyDiscounts() {
       
        System.out.println("We didnt have time");
    }

    private void addNewCustomer() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add New Customer");
        nameDialog.setHeaderText("Enter customer name:");
        nameDialog.setContentText("Name:");

        Optional<String> nameResult = nameDialog.showAndWait();
        nameResult.ifPresent(name -> {
            TextInputDialog contactDialog = new TextInputDialog();
            contactDialog.setTitle("Add New Customer");
            contactDialog.setHeaderText("Enter contact details:");
            contactDialog.setContentText("Contact Details:");

            Optional<String> contactResult = contactDialog.showAndWait();
            contactResult.ifPresent(contactDetails -> {
                TextInputDialog purchaseDialog = new TextInputDialog();
                purchaseDialog.setTitle("Add New Customer");
                purchaseDialog.setHeaderText("Enter purchase history:");
                purchaseDialog.setContentText("Purchase History:");

                Optional<String> purchaseResult = purchaseDialog.showAndWait();
                purchaseResult.ifPresent(purchaseHistory -> {
                   
                    Customer newCustomer = new Customer(name, contactDetails, purchaseHistory);

                 
                    addCustomerToFile(newCustomer);

                    refreshTableView();
                });
            });
        });
    }

    private void refreshTableView() {
    
        ObservableList<Customer> data = FXCollections.observableArrayList(customers);
        tableView.setItems(data);
    }

    private void loadCustomersFromFile() {
    
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("customers.txt"))) {
            customers = (List<Customer>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing customers file found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addCustomerToFile(Customer customer) {
        customers.add(customer);
        saveCustomersToFile();
    }

    static void saveCustomersToFile() {
       
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("customers.txt"))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Customer implements Serializable {
        private String name;
        private String contactDetails;
        private String purchaseHistory;

        public Customer(String name, String contactDetails, String purchaseHistory) {
            this.name = name;
            this.contactDetails = contactDetails;
            this.purchaseHistory = purchaseHistory;
        }

        public String getName() {
            return name;
        }

        public String getContactDetails() {
            return contactDetails;
        }

        public String getPurchaseHistory() {
            return purchaseHistory;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setContactDetails(String contactDetails) {
            this.contactDetails = contactDetails;
        }

        public void setPurchaseHistory(String purchaseHistory) {
            this.purchaseHistory = purchaseHistory;
        }
        
    }
}
