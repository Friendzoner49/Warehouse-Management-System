package project1.model;

import javafx.beans.property.*;

public class OrderItem {
    private final IntegerProperty productId;
    private final StringProperty name;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    
    public OrderItem(int productId, String name, int quantity, double price) {
        this.productId = new SimpleIntegerProperty(productId);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
    }
    
    public int getProductId() { return productId.get(); }
    public String getName() { return name.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getPrice() { return price.get(); }
    
    public IntegerProperty productIdProperty() { return productId; }
    public StringProperty nameProperty() { return name; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty priceProperty() { return price; }
    
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
}