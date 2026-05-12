package project1.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import project1.dao.*;
import project1.model.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.sql.*;
import java.util.Optional;

import project1.util.DBConnection;

public class ManagerView {
    private Stage stage;
    private User user;
    private TabPane tabPane;
    
    
    
    private TableView<Customer> customersTable;
    private TableView<Order> ordersTable;
    private TableView<Invoice> invoicesTable;
    
    private int currentUserId;
    public ManagerView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.currentUserId = user.getId();
    }
    
    public void show() {
        // Background
        Image backgroundInput = new Image(getClass().getResourceAsStream("/resources/2.jpg"));
        ImageView background = new ImageView(backgroundInput);
        background.setFitWidth(1000);
        background.setFitHeight(700);
        background.setPreserveRatio(false);
        
        tabPane = new TabPane();
        tabPane.getTabs().addAll(
            createCustomersTab(),
            createOrdersTab(),
            createInvoicesTab()
        );
        tabPane.setPrefSize(1000, 700);
        
        StackPane root = new StackPane();
        root.getChildren().addAll(background, tabPane);
        
        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("ADMIN PANEL - " + user.getUsername());
        stage.setScene(scene);
        stage.show();
    }
    
    
    
    private Tab createCustomersTab() {
        Tab tab = new Tab("Customers");
        
        customersTable = new TableView<>();
        customersTable.setItems(CustomerDAO.getAllCustomers());
        
        TableColumn<Customer, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colId.setPrefWidth(50);
        
        TableColumn<Customer, String> colName = new TableColumn<>("Full Name");
        colName.setCellValueFactory(data -> data.getValue().fullNameProperty());
        colName.setPrefWidth(150);
        
        TableColumn<Customer, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(data -> data.getValue().phoneProperty());
        colPhone.setPrefWidth(120);
        
        TableColumn<Customer, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        colEmail.setPrefWidth(150);
        
        TableColumn<Customer, String> colAddress = new TableColumn<>("Address");
        colAddress.setCellValueFactory(data -> data.getValue().addressProperty());
        colAddress.setPrefWidth(200);
        
        customersTable.getColumns().addAll(colId, colName, colPhone, colEmail, colAddress);
        
        Button btnAdd = new Button("➕ Add");
        Button btnEdit = new Button("✏️ Edit");
        Button btnDelete = new Button("❌ Delete");
        Button btnRefresh = new Button("🔄 Refresh");
        
        btnAdd.setOnAction(e -> showCustomerDialog(null));
        btnEdit.setOnAction(e -> {
            Customer selected = customersTable.getSelectionModel().getSelectedItem();
            if (selected != null) showCustomerDialog(selected);
            else showAlert("Please select a customer!");
        });
        btnDelete.setOnAction(e -> {
            Customer selected = customersTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (confirm("Delete customer '" + selected.getFullName() + "'?")) {
                    CustomerDAO.deleteCustomer(selected.getId());
                    customersTable.setItems(CustomerDAO.getAllCustomers());
                }
            } else {
                showAlert("Please select a customer!");
            }
        });
        btnRefresh.setOnAction(e -> customersTable.setItems(CustomerDAO.getAllCustomers()));
        
        HBox btnBox = new HBox(10, btnAdd, btnEdit, btnDelete, btnRefresh);
        btnBox.setPadding(new Insets(10));
        
        VBox layout = new VBox(10, customersTable, btnBox);
        layout.setPadding(new Insets(10));
        tab.setContent(layout);
        
        return tab;
    }
    
    private Tab createOrdersTab() {
        Tab tab = new Tab("Orders");
        
        ordersTable = new TableView<>();
        ordersTable.setItems(OrderDAO.getAllOrders());
        
        TableColumn<Order, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colId.setPrefWidth(50);
        
        TableColumn<Order, String> colCustomer = new TableColumn<>("Customer");
        colCustomer.setCellValueFactory(data -> data.getValue().customerNameProperty());
        colCustomer.setPrefWidth(180);
        
        TableColumn<Order, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getOrderDate().toString()
            )
        );
        colDate.setPrefWidth(100);
        
        TableColumn<Order, Double> colTotal = new TableColumn<>("Total Amount");
        colTotal.setCellValueFactory(data -> data.getValue().totalAmountProperty().asObject());
        colTotal.setCellFactory(column -> new TableCell<Order, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f ₽", item));
            }
        });
        colTotal.setPrefWidth(120);
        
        ordersTable.getColumns().addAll(colId, colCustomer, colDate, colTotal);
        
        // ✅ THÊM CÁC NÚT ĐIỀU KHIỂN
        Button btnAdd = new Button("➕ Add Order");
        Button btnView = new Button("🔍 View Details");
        Button btnStatus = new Button("📝 Edit Status");
        Button btnDelete = new Button("❌ Delete Order");
        Button btnRefresh = new Button("🔄 Refresh");
        
        // Style cho nút
        btnAdd.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnView.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        btnDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        btnStatus.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        // Disable các nút khi chưa chọn đơn
        btnView.setDisable(true);
        btnStatus.setDisable(true);
        btnDelete.setDisable(true);
        
        // ✅ Sự kiện khi chọn đơn hàng
        ordersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean selected = (newVal != null);
            btnView.setDisable(!selected);
            btnStatus.setDisable(!selected);
            btnDelete.setDisable(!selected);
        });
        
        // ✅ Sự kiện nút Add
        btnAdd.setOnAction(e -> {
            OrderCreationDialog dialog = new OrderCreationDialog(currentUserId);
            dialog.show();
            ordersTable.setItems(OrderDAO.getAllOrders()); // Refresh sau khi thêm
        });
        
        // ✅ Sự kiện nút View Details
        btnView.setOnAction(e -> {
            Order selected = ordersTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showOrderDetails(selected);
            }
        });
        
        btnStatus.setOnAction(e -> {
            Order selected = ordersTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                new OrderStatusDialog(selected, currentUserId).show();
                ordersTable.setItems(OrderDAO.getAllOrders()); // Refresh bảng sau khi lưu
            }
        });
        //  Sự kiện nút Delete
        btnDelete.setOnAction(e -> {
            Order selected = ordersTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (confirm("Delete order #" + selected.getId() + "?\n\n" +
                           "Customer: " + selected.getCustomerName() + "\n" +
                           "Total: " + String.format("%.2f ₽", selected.getTotalAmount()))) {
                    
                    // Xóa các tracking items trước
                    TrackingDAO.deleteTrackingByOrderId(selected.getId());
                    
                    // Xóa invoice nếu có
                    InvoiceDAO.deleteInvoiceByOrderId(selected.getId());
                    
                    // Xóa đơn hàng
                    OrderDAO.deleteOrder(selected.getId());
                    
                    // Refresh table
                    ordersTable.setItems(OrderDAO.getAllOrders());
                    
                    showAlert("✅ Order #" + selected.getId() + " has been deleted!");
                }
            } else {
                showAlert("⚠️ Please select an order to delete!");
            }
        });
        
        // ✅ Sự kiện nút Refresh
        btnRefresh.setOnAction(e -> ordersTable.setItems(OrderDAO.getAllOrders()));
        
        // Double-click để xem chi tiết
        ordersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Order selected = ordersTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showOrderDetails(selected);
                }
            }
        });
        
        // ✅ Layout cho các nút
        HBox btnBox = new HBox(10, btnAdd, btnView, btnStatus, btnDelete, btnRefresh);
        btnBox.setPadding(new Insets(10));
        btnBox.setAlignment(Pos.CENTER);
        
        VBox layout = new VBox(10, ordersTable, btnBox);
        layout.setPadding(new Insets(10));
        tab.setContent(layout);

        
        return tab;
    }
    
    private Tab createInvoicesTab() {
        Tab tab = new Tab("Invoices");
        
        invoicesTable = new TableView<>();
        invoicesTable.setItems(InvoiceDAO.getAllInvoices());
        
        TableColumn<Invoice, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colId.setPrefWidth(50);
        
        TableColumn<Invoice, Integer> colOrderId = new TableColumn<>("Order ID");
        colOrderId.setCellValueFactory(data -> data.getValue().orderIdProperty().asObject());
        colOrderId.setPrefWidth(80);
        
        TableColumn<Invoice, String> colDate = new TableColumn<>("Issue Date");
        colDate.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getIssueDate().toString()
            )
        );
        colDate.setPrefWidth(100);
        
        TableColumn<Invoice, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> data.getValue().paymentStatusProperty());
        colStatus.setPrefWidth(100);
        
        invoicesTable.getColumns().addAll(colId, colOrderId, colDate, colStatus);
        
        Button btnRefresh = new Button("🔄 Refresh");
        btnRefresh.setOnAction(e -> invoicesTable.setItems(InvoiceDAO.getAllInvoices()));
        
        HBox btnBox = new HBox(10, btnRefresh);
        btnBox.setPadding(new Insets(10));
        
        VBox layout = new VBox(10, invoicesTable, btnBox);
        layout.setPadding(new Insets(10));
        tab.setContent(layout);
        
        return tab;
    }
    
    private void showOrderDetails(Order order) {
        Stage dialog = new Stage();
        dialog.setTitle("Order Details #" + order.getId());
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        // ✅ Lấy thông tin thanh toán từ invoices
        Invoice invoice = InvoiceDAO.getInvoiceByOrderId(order.getId());
        String paymentStatus = (invoice != null) ? invoice.getPaymentStatus() : "NO_INVOICE";
        
        // Thông tin đơn hàng
        VBox infoBox = new VBox(10,
            new Label("📋 Order ID: " + order.getId()),
            new Label("👤 Customer: " + order.getCustomerName()),
            new Label("📅 Date: " + order.getOrderDate().toString()),
            new Label("💰 Total Amount: " + String.format("%.2f ₽", order.getTotalAmount())),
            new Separator(),
            new Label("📦 Delivery Status: " + TrackingDAO.getOrderDeliveryStatus(order.getId())),
            new Label(" Payment Status: " + paymentStatus)  // ✅ Hiển thị trạng thái thanh toán
        );
        infoBox.setPadding(new Insets(15));
        infoBox.setStyle("-fx-background-color: #f0f8ff; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        // Bảng tracking
        Label lblTracking = new Label("📦 Order Items:");
        lblTracking.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        TableView<OrderTracking> trackingTable = new TableView<>();
        trackingTable.setItems(TrackingDAO.getTrackingByOrderId(order.getId()));
        
        TableColumn<OrderTracking, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colId.setPrefWidth(50);
        
        TableColumn<OrderTracking, String> colProduct = new TableColumn<>("Product");
        colProduct.setCellValueFactory(data -> data.getValue().productNameProperty());
        colProduct.setPrefWidth(200);
        
        TableColumn<OrderTracking, Integer> colQty = new TableColumn<>("Qty");
        colQty.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        colQty.setPrefWidth(80);
        
        TableColumn<OrderTracking, Double> colPrice = new TableColumn<>("Unit Price");
        colPrice.setCellValueFactory(data -> data.getValue().unitPriceProperty().asObject());
        colPrice.setPrefWidth(100);
        
        TableColumn<OrderTracking, Double> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(data -> data.getValue().totalPriceProperty().asObject());
        colTotal.setPrefWidth(100);
        
        TableColumn<OrderTracking, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colStatus.setPrefWidth(120);
        
        trackingTable.getColumns().addAll(colId, colProduct, colQty, colPrice, colTotal, colStatus);
        
        // Tính doanh thu
        double revenue = TrackingDAO.getOrderTotal(order.getId());
        Label lblRevenue = new Label("💵 Total Revenue: " + String.format("%.2f ₽", revenue));
        lblRevenue.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2a5298;");
        
        // Nút đóng
        Button btnClose = new Button("Close");
        btnClose.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
        btnClose.setOnAction(e -> dialog.close());
        
        VBox layout = new VBox(15,
            new Label("ORDER DETAILS"),
            infoBox,
            lblTracking,
            trackingTable,
            lblRevenue,
            btnClose
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);
        
        Scene scene = new Scene(layout, 800, 650);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private void showCustomerDialog(Customer customer) {
        Stage dialog = new Stage();
        dialog.setTitle(customer == null ? "Add Customer" : "Edit Customer");
        
        TextField txtName = new TextField(customer == null ? "" : customer.getFullName());
        TextField txtPhone = new TextField(customer == null ? "" : customer.getPhone());
        TextField txtEmail = new TextField(customer == null ? "" : customer.getEmail());
        TextField txtAddress = new TextField(customer == null ? "" : customer.getAddress());
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.addRow(0, new Label("Full Name:"), txtName);
        grid.addRow(1, new Label("Phone:"), txtPhone);
        grid.addRow(2, new Label("Email:"), txtEmail);
        grid.addRow(3, new Label("Address:"), txtAddress);
        
        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");
        
        btnSave.setOnAction(e -> {
            try {
                String name = txtName.getText().trim();
                String phone = txtPhone.getText().trim();
                String email = txtEmail.getText().trim();
                String address = txtAddress.getText().trim();
                
                if (customer == null) {
                    CustomerDAO.addCustomer(name, phone, email, address);
                } else {
                    CustomerDAO.updateCustomer(customer.getId(), name, phone, email, address);
                }
                customersTable.setItems(CustomerDAO.getAllCustomers());
                dialog.close();
            } catch (Exception ex) {
                showAlert("Error: " + ex.getMessage());
            }
        });
        
        btnCancel.setOnAction(e -> dialog.close());
        
        HBox btnBox = new HBox(10, btnSave, btnCancel);
        btnBox.setPadding(new Insets(10));
        
        VBox root = new VBox(20, grid, btnBox);
        dialog.setScene(new Scene(root, 300, 280));
        dialog.showAndWait();
    }
    
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    private boolean confirm(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setContentText(msg);
        return alert.showAndWait().get() == ButtonType.OK;
    }
    
}