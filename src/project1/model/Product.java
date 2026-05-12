package project1.model;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty unit;
    private final DoubleProperty price;
    private final IntegerProperty stockQty;
    
    // Constructor đầy đủ 5 tham số
    public Product(int id, String name, String unit, double price, int stockQty) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.unit = new SimpleStringProperty(unit);
        this.price = new SimpleDoubleProperty(price);
        this.stockQty = new SimpleIntegerProperty(stockQty);
    }
    
    // Constructor không có id (dùng khi thêm mới)
    public Product(String name, String unit, double price, int stockQty) {
        this.id = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty(name);
        this.unit = new SimpleStringProperty(unit);
        this.price = new SimpleDoubleProperty(price);
        this.stockQty = new SimpleIntegerProperty(stockQty);
    }
    
    // Getters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getUnit() { return unit.get(); }
    public double getPrice() { return price.get(); }
    public int getStockQty() { return stockQty.get(); }
    
    // Properties
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty unitProperty() { return unit; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty stockQtyProperty() { return stockQty; }
    
    // Setters
    public void setName(String name) { this.name.set(name); }
    public void setUnit(String unit) { this.unit.set(unit); }
    public void setPrice(double price) { this.price.set(price); }
    public void setStockQty(int stockQty) { this.stockQty.set(stockQty); }
    
    @Override
    public String toString() {
        return name.get();
    }
}