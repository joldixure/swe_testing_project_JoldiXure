
package First;

import CostumerManagment.CustomerManagement;
import POS.PointOfSale;
import Sales.SalesReportManager;
import Sales.SalesReportsWindow;
import inventory.InventoryManagement;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private PointOfSale pointOfSale;
    private CustomerManagement customerManagement;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main Page");

        // Create a layout for the main page
        GridPane mainLayout = createMainPageLayout(primaryStage, "");

        // Create a scene and add the layout to the scene
        Scene scene = new Scene(mainLayout, 600, 400);

        // Set the scene to the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage
        primaryStage.show();
    }

    // Method to create the layout of the main page
    public GridPane createMainPageLayout(Stage primaryStage, String userRole) {
        GridPane mainLayout = new GridPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setHgap(10);
        mainLayout.setVgap(10);

        Text welcomeText = new Text("Welcome to ED Store!");
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        mainLayout.add(welcomeText, 0, 0, 2, 1);

        // Add buttons or other necessary elements for your application
        Button inventoryButton = createStyledButton("Inventory Management", Color.rgb(52, 152, 219));
        Button posButton = createStyledButton("Point of Sale System", Color.rgb(231, 76, 60));
        Button customerButton = createStyledButton("Customer Management", Color.rgb(243, 156, 18));
        Button salesReportButton = createStyledButton("Sales Reports", Color.rgb(155, 89, 182));

        // Set actions for each button as needed
        inventoryButton.setOnAction(e -> openInventoryManagement());
        posButton.setOnAction(e -> openPointOfSale(primaryStage));
        customerButton.setOnAction(e -> openCustomerManagement(primaryStage));
        salesReportButton.setOnAction(e -> openSalesReports(primaryStage));

        // Add buttons to the layout based on the user role
        if (!userRole.equals("admin")) {
            // If userRole is not admin, show POS and Customer Management buttons
            mainLayout.add(posButton, 0, 1);
            mainLayout.add(customerButton, 1, 1);
        } else {
            // If userRole is admin, show all buttons
            mainLayout.add(inventoryButton, 0, 1);
            mainLayout.add(posButton, 1, 1);
            mainLayout.add(customerButton, 0, 2);
            mainLayout.add(salesReportButton, 1, 2);
        }

        return mainLayout;
    }

    private Button createStyledButton(String text, Color backgroundColor) {
        Button button = new Button(text);
        button.setMinWidth(200);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #" + backgroundColor.toString().substring(2));

        button.setOnMouseEntered(e -> button.setBackground(
                new Background(new BackgroundFill(backgroundColor.darker(), CornerRadii.EMPTY, Insets.EMPTY))));
        button.setOnMouseExited(e -> button.setBackground(
                new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY))));

        return button;
    }

    private void openPointOfSale(Stage primaryStage) {
        if (pointOfSale == null) {
            pointOfSale = new PointOfSale();
            pointOfSale.setSalesReportManager(new SalesReportManager());
        }
        pointOfSale.start(new Stage());
        primaryStage.hide();  // Hide the main page
    }

    private void openCustomerManagement(Stage primaryStage) {
        if (customerManagement == null) {
            customerManagement = new CustomerManagement();
        }
        customerManagement.start(new Stage());
        primaryStage.hide();
    }

    // Method to show the main page
    public void showMainPage(Stage primaryStage, String userRole) {
        Scene mainScene = new Scene(createMainPageLayout(primaryStage, userRole), 600, 400);
        primaryStage.setScene(mainScene);
    }
    
    public void openInventoryManagement() {
        // Create a new stage for the InventoryManagement window
        Stage inventoryStage = new Stage();

        // Create an instance of InventoryManagement
        InventoryManagement inventoryManagement = new InventoryManagement();

        // Call the start method of InventoryManagement with the new stage
        inventoryManagement.start(inventoryStage);

        // Show the new stage (InventoryManagement window)
        inventoryStage.show();
    }
    private void openSalesReports(Stage primaryStage) {
        SalesReportManager salesReportManager = new SalesReportManager();
        SalesReportsWindow salesReportsWindow = new SalesReportsWindow(salesReportManager);
        salesReportsWindow.showSalesReports(primaryStage);
    }

}
