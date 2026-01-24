/**
 * 
 */
/**
 * 
 */
module FinalProjectJava {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;
	requires org.junit.jupiter.api;

    opens First to javafx.fxml;
    opens CostumerManagment to javafx.fxml; 
    opens inventory to javafx.fxml;

    exports First;
    exports CostumerManagment;
    exports inventory;
}