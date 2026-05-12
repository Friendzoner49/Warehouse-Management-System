package project1.model;

import javafx.beans.property.*;

public class Customer {
    private final IntegerProperty id;
    private final StringProperty fullName;
    private final StringProperty phone;
    private final StringProperty email;
    private final StringProperty address;
    
    public Customer(int id, String fullName, String phone, String email, String address) {
        this.id = new SimpleIntegerProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
    }
    
    public Customer(String fullName, String phone, String email, String address) {
        this.id = new SimpleIntegerProperty(0);
        this.fullName = new SimpleStringProperty(fullName);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
    }
    
    // Getters
    public int getId() { return id.get(); }
    public String getFullName() { return fullName.get(); }
    public String getPhone() { return phone.get(); }
    public String getEmail() { return email.get(); }
    public String getAddress() { return address.get(); }
    
    // Properties
    public IntegerProperty idProperty() { return id; }
    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty emailProperty() { return email; }
    public StringProperty addressProperty() { return address; }
    
    // Setters
    public void setFullName(String fullName) { this.fullName.set(fullName); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public void setEmail(String email) { this.email.set(email); }
    public void setAddress(String address) { this.address.set(address); }
}