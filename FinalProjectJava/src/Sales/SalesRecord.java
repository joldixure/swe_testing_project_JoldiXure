package Sales;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesRecord {
    private String itemName;
    private int quantity;
    private double totalCost;
    private Date saleDate;

    public SalesRecord(String itemName, int quantity, double totalCost, Date saleDate) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.saleDate = saleDate;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Item: " + itemName +
                ", Quantity: " + quantity +
                ", Total Cost: $" + totalCost +
                ", Sale Date: " + dateFormat.format(saleDate);
    }

    public SimpleStringProperty itemNameProperty() {
        return new SimpleStringProperty(itemName);
    }

    public SimpleIntegerProperty quantityProperty() {
        return new SimpleIntegerProperty(quantity);
    }

    public SimpleDoubleProperty totalCostProperty() {
        return new SimpleDoubleProperty(totalCost);
    }

    public SimpleStringProperty formattedSaleDateProperty() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(saleDate);
        return new SimpleStringProperty(formattedDate);
    }
}