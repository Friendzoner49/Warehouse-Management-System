package project1.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Invoice {
    private final IntegerProperty id;
    private final IntegerProperty orderId;
    private final ObjectProperty<LocalDate> issueDate;
    private final IntegerProperty issuedBy;
    private final StringProperty paymentStatus; // UNPAID hoặc PAID
    
    public Invoice(int id, int orderId, LocalDate issueDate, int issuedBy, String paymentStatus) {
        this.id = new SimpleIntegerProperty(id);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.issueDate = new SimpleObjectProperty<>(issueDate);
        this.issuedBy = new SimpleIntegerProperty(issuedBy);
        this.paymentStatus = new SimpleStringProperty(paymentStatus);
    }
    
    public Invoice(int orderId, LocalDate issueDate, int issuedBy, String paymentStatus) {
        this.id = new SimpleIntegerProperty(0);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.issueDate = new SimpleObjectProperty<>(issueDate);
        this.issuedBy = new SimpleIntegerProperty(issuedBy);
        this.paymentStatus = new SimpleStringProperty(paymentStatus);
    }
    
    // Getters
    public int getId() { return id.get(); }
    public int getOrderId() { return orderId.get(); }
    public LocalDate getIssueDate() { return issueDate.get(); }
    public int getIssuedBy() { return issuedBy.get(); }
    public String getPaymentStatus() { return paymentStatus.get(); }
    
    // Properties
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty orderIdProperty() { return orderId; }
    public ObjectProperty<LocalDate> issueDateProperty() { return issueDate; }
    public IntegerProperty issuedByProperty() { return issuedBy; }
    public StringProperty paymentStatusProperty() { return paymentStatus; }
    
    // Setters
    public void setPaymentStatus(String status) { this.paymentStatus.set(status); }
}