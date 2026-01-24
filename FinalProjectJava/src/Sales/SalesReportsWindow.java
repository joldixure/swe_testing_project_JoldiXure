package Sales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SalesReportsWindow {
    private SalesReportManager salesReportManager;

    public SalesReportsWindow(SalesReportManager salesReportManager) {
        this.salesReportManager = salesReportManager;
    }

    public void showSalesReports(Stage primaryStage) {
        primaryStage.setTitle("Sales Reports");

        // Create a TableView to display sales reports
        TableView<SalesRecord> tableView = new TableView<>();
        ObservableList<Sales.SalesReportManager.SalesRecord> data = FXCollections.observableArrayList(salesReportManager.getSalesRecords());

       // Create columns for the TableView
        TableColumn<SalesRecord, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());

        TableColumn<SalesRecord, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        TableColumn<SalesRecord, Double> totalCostCol = new TableColumn<>("Total Cost");
        totalCostCol.setCellValueFactory(cellData -> cellData.getValue().totalCostProperty().asObject());

        TableColumn<SalesRecord, String> saleDateCol = new TableColumn<>("Sale Date");
        saleDateCol.setCellValueFactory(cellData -> cellData.getValue().formattedSaleDateProperty());

        tableView.getColumns().addAll(itemNameCol, quantityCol, totalCostCol, saleDateCol);
        tableView.setId("salesReportsTableView");

        VBox vbox = new VBox(tableView);
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
