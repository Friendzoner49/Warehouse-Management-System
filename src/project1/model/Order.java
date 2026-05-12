package project1.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Order {
    private final IntegerProperty id;
    private final IntegerProperty customerId;
    private final StringProperty customerName;
    private final ObjectProperty<LocalDate> orderDate;
    private final IntegerProperty createdBy;
    private final DoubleProperty totalAmount;
    
    // Constructor đầy đủ 6 tham số
    public Order(int id, int customerId, String customerName, LocalDate orderDate, int createdBy, double totalAmount) {
        this.id = new SimpleIntegerProperty(id);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.orderDate = new SimpleObjectProperty<>(orderDate);
        this.createdBy = new SimpleIntegerProperty(createdBy);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
    }
    
    // Constructor không có id
    public Order(int customerId, String customerName, LocalDate orderDate, int createdBy, double totalAmount) {
        this.id = new SimpleIntegerProperty(0);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.orderDate = new SimpleObjectProperty<>(orderDate);
        this.createdBy = new SimpleIntegerProperty(createdBy);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
    }
    
    // Getters
    public int getId() { return id.get(); }
    public int getCustomerId() { return customerId.get(); }
    public String getCustomerName() { return customerName.get(); }
    public LocalDate getOrderDate() { return orderDate.get(); }
    public int getCreatedBy() { return createdBy.get(); }
    public double getTotalAmount() { return totalAmount.get(); }
    
    // Properties
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty customerIdProperty() { return customerId; }
    public StringProperty customerNameProperty() { return customerName; }
    public ObjectProperty<LocalDate> orderDateProperty() { return orderDate; }
    public IntegerProperty createdByProperty() { return createdBy; }
    public DoubleProperty totalAmountProperty() { return totalAmount; }
    
    // Setters
    public void setId(int id) { this.id.set(id); }          
    public void setCustomerId(int customerId) { this.customerId.set(customerId); }
    public void setCustomerName(String customerName) { this.customerName.set(customerName); }
    public void setOrderDate(LocalDate orderDate) { this.orderDate.set(orderDate); }
    public void setCreatedBy(int createdBy) { this.createdBy.set(createdBy); }
    public void setTotalAmount(double totalAmount) { this.totalAmount.set(totalAmount); }
}