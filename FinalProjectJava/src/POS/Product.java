package POS;

import javafx.beans.property.*;

import java.io.*;
import java.util.function.Consumer;

public class Product {
	 private final IntegerProperty id;
	    private final StringProperty name;
	    private final StringProperty category;
	    private final DoubleProperty price;
	    private final IntegerProperty quantityInStock;
	    private final StringProperty supplierInfo;
    
	    public Product(int id, String name, String category, double price, int quantityInStock, String supplierInfo) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleDoubleProperty(price);
        this.quantityInStock = new SimpleIntegerProperty(quantityInStock);
        this.supplierInfo = new SimpleStringProperty(supplierInfo);
    }

    // Getter methods...
    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getCategory() {
        return category.get();
    }

    public double getPrice() {
        return price.get();
    }

    public int getQuantityInStock() {
        return quantityInStock.get();
    }

    public String getSupplierInfo() {
        return supplierInfo.get();
    }

    // Setter methods...
    public void setName(String name) {
        this.name.set(name);
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock.set(quantityInStock);
    }

    public void setSupplierInfo(String supplierInfo) {
        this.supplierInfo.set(supplierInfo);
    }

    // Property methods...
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public IntegerProperty quantityInStockProperty() {
        return quantityInStock;
    }

    public StringProperty supplierInfoProperty() {
        return supplierInfo;
    }

    // Metoda p�r t� shqyrtuar produktin
    public void ifPresent(Consumer<Product> consumer) {
        if (this != null) {
            consumer.accept(this);
        }
    }

    // Metoda p�r t� shkruar produktin n� file
    public void writeToFile(BufferedWriter writer) throws IOException {
        writer.write(name.get());
        writer.newLine();
    }

    // Metoda p�r t� lexuar produktin nga file
    public static Product readFromFile(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            return new Product(0, line, null, 0.0, 0, null);
        }
        return null;
    }
    // Metoda toString p�r t� shfaqur informacionin e produktit
    @Override
    public String toString() {
        return name.get(); // Assuming you have a getName() method in your Product class
    }
}
