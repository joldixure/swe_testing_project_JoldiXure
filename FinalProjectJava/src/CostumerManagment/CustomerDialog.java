
package CostumerManagment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class CustomerDialog {

    public static void showCustomers(List<Customer> customers) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("Customer Information");

        TableView<Customer> tableView = createTableView(customers);

        VBox vbox = new VBox(tableView);

        Scene dialogScene = new Scene(vbox, 400, 300);
        dialogStage.setScene(dialogScene);

        dialogStage.showAndWait();
    }

    private static TableView<Customer> createTableView(List<Customer> customers) {
        TableView<Customer> tableView = new TableView<>();
        tableView.setEditable(false);

        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> contactDetailsColumn = new TableColumn<>("Contact Details");
        contactDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("contactDetails"));

        TableColumn<Customer, String> purchaseHistoryColumn = new TableColumn<>("Purchase History");
        purchaseHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseHistory"));

        tableView.getColumns().addAll(nameColumn, contactDetailsColumn, purchaseHistoryColumn);

        ObservableList<Customer> data = FXCollections.observableArrayList(customers);
        tableView.setItems(data);

        return tableView;
    }
}
