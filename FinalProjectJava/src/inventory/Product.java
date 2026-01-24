package inventory;

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

    
    public void ifPresent(Consumer<Product> consumer) {
        if (this != null) {
            consumer.accept(this);
        }
    }

    
    public void writeToFile(BufferedWriter writer) throws IOException {
        writer.write(id.get() + "," +
                name.get() + "," +
                category.get() + "," +
                price.get() + "," +
                quantityInStock.get() + "," +
                supplierInfo.get());
        writer.newLine();
    }

    
    public static Product readFromFile(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String category = parts[2];
                double price = Double.parseDouble(parts[3]);
                int quantityInStock = Integer.parseInt(parts[4]);
                String supplierInfo = parts[5];
                return new Product(id, name, category, price, quantityInStock, supplierInfo);
            }
        }
        return null;
    }

    
    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", category=" + category +
                ", price=" + price + ", quantityInStock=" + quantityInStock +
                ", supplierInfo=" + supplierInfo + "]";
    }
}
