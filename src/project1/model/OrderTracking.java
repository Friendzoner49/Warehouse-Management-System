package project1.model;

import javafx.beans.property.*;

public class OrderTracking {
    private final IntegerProperty id;
    private final IntegerProperty orderId;
    private final IntegerProperty productId;
    private final StringProperty productName;
    private final IntegerProperty quantity;
    private final DoubleProperty unitPrice;
    private final DoubleProperty totalPrice;
    private final StringProperty status;
    
    public OrderTracking(int id, int orderId, int productId, String productName, 
                        int quantity, double unitPrice, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.totalPrice = new SimpleDoubleProperty(quantity * unitPrice);
        this.status = new SimpleStringProperty(status);
    }
    
    public OrderTracking(int orderId, int productId, String productName, 
                        int quantity, double unitPrice, String status) {
        this.id = new SimpleIntegerProperty(0);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.totalPrice = new SimpleDoubleProperty(quantity * unitPrice);
        this.status = new SimpleStringProperty(status);
    }
    
    // Getters
    public int getId() { return id.get(); }
    public int getOrderId() { return orderId.get(); }
    public int getProductId() { return productId.get(); }
    public String getProductName() { return productName.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getUnitPrice() { return unitPrice.get(); }
    public double getTotalPrice() { return totalPrice.get(); }
    public String getStatus() { return status.get(); }
    
    // Properties
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty orderIdProperty() { return orderId; }
    public IntegerProperty productIdProperty() { return productId; }
    public StringProperty productNameProperty() { return productName; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty unitPriceProperty() { return unitPrice; }
    public DoubleProperty totalPriceProperty() { return totalPrice; }
    public StringProperty statusProperty() { return status; }
    
    // Setters
    public void setQuantity(int quantity) { 
        this.quantity.set(quantity); 
        this.totalPrice.set(quantity * unitPrice.get());
    }
    public void setStatus(String status) { this.status.set(status); }
}