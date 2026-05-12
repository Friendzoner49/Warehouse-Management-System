package project1.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project1.dao.*;
import project1.model.*;
import java.time.LocalDate;

public class OrderCreationDialog {
    private Stage dialog;
    private ObservableList<OrderItem> items;
    private TableView<OrderItem> itemsTable;
    private TextField txtSearchProduct;
    private ListView<Product> lstProducts;
    private TextField txtCustomer;
    private TextField txtQuantity;
    private DatePicker dpDate;
    private int currentUserId;
    
    // Filtered list cho sản phẩm
    private ObservableList<Product> allProducts;
    private FilteredList<Product> filteredProducts;
    
    public OrderCreationDialog(int userId) {
        this.currentUserId = userId;
        this.items = FXCollections.observableArrayList();
        buildUI();
    }
    
    private void buildUI() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Create New Order");
        
        // Tiêu đề
        Label lblTitle = new Label("NEW ORDER");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2a5298;");
        
        // Thông tin khách hàng
        Label lblCustomer = new Label("Customer (Phone/Email):");
        lblCustomer.setStyle("-fx-font-weight: bold;");
        txtCustomer = new TextField();
        txtCustomer.setPromptText("Enter customer phone or email");
        
        dpDate = new DatePicker(LocalDate.now());
        
        VBox infoBox = new VBox(10,
            lblCustomer, txtCustomer,
            new Label("Order Date:"), dpDate
        );
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        // Phần tìm kiếm sản phẩm
        Label lblProduct = new Label("Search & Add Products:");
        lblProduct.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        txtSearchProduct = new TextField();
        txtSearchProduct.setPromptText("Type to search product by name...");
        txtSearchProduct.setPrefWidth(400);
        txtSearchProduct.setStyle("-fx-font-size: 12px;");
        
        // ListView để hiển thị kết quả tìm kiếm
        lstProducts = new ListView<>();
        lstProducts.setPrefHeight(180);
        lstProducts.setPrefWidth(400);
        
        // Load tất cả sản phẩm
        allProducts = ProductDAO.getAllProducts();
        filteredProducts = new FilteredList<>(allProducts, p -> true);
        lstProducts.setItems(filteredProducts);
        
        // Format hiển thị trong ListView
        lstProducts.setCellFactory(listView -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%s\nPrice: %.2f ₽ | Stock: %d units", 
                        product.getName(), product.getPrice(), product.getStockQty()));
                    // Highlight nếu sắp hết hàng
                    if (product.getStockQty() < 10) {
                        setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828;");
                    } else if (product.getStockQty() < 20) {
                        setStyle("-fx-background-color: #fff3e0;");
                    }
                }
            }
        });
        
        // Lọc sản phẩm khi gõ
        txtSearchProduct.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase().trim();
            if (filter == null || filter.isEmpty()) {
                filteredProducts.setPredicate(p -> true);
            } else {
                filteredProducts.setPredicate(p -> 
                    p.getName().toLowerCase().contains(filter)
                );
            }
        });
        
        txtQuantity = new TextField("1");
        txtQuantity.setPrefWidth(80);
        
        Button btnAddProduct = new Button("Add to Order");
        btnAddProduct.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAddProduct.setOnAction(e -> {
            Product selected = lstProducts.getSelectionModel().getSelectedItem();
            if (selected != null) {
                addProductToOrder(selected);
            } else {
                showAlert("Please select a product from the list!");
            }
        });
        
        // Double-click để thêm nhanh
        lstProducts.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Product selected = lstProducts.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    addProductToOrder(selected);
                }
            }
        });
        
        HBox addProductBox = new HBox(10, 
            new Label("Qty:"), txtQuantity, btnAddProduct
        );
        addProductBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox productBox = new VBox(10, 
            lblProduct,
            txtSearchProduct,
            lstProducts,
            addProductBox
        );
        productBox.setPadding(new Insets(10));
        productBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        // Bảng sản phẩm trong đơn
        Label lblOrderItems = new Label("Products in Order:");
        lblOrderItems.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        itemsTable = new TableView<>();
        itemsTable.setItems(items);
        setupItemsTable();
        itemsTable.setPrefHeight(200);
        
        // Tổng tiền
        Label lblTotal = new Label("Total Amount: 0.00 ₽");
        lblTotal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a5298;");
        
        // Nút điều khiển
        Button btnSave = new Button("Save Order");
        Button btnCancel = new Button("Cancel");
        
        btnSave.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20 8 20;");
        btnCancel.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20 8 20;");
        
        btnSave.setOnAction(e -> saveOrder());
        btnCancel.setOnAction(e -> dialog.close());
        
        HBox btnBox = new HBox(15, btnSave, btnCancel);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10));
        
        // Layout chính
        VBox layout = new VBox(15,
            lblTitle,
            new Separator(),
            infoBox,
            new Separator(),
            productBox,
            new Separator(),
            lblOrderItems,
            itemsTable,
            lblTotal,
            new Separator(),
            btnBox
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);
        
        // Update total khi có thay đổi
        items.addListener((javafx.collections.ListChangeListener.Change<? extends OrderItem> c) -> {
            double total = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
            lblTotal.setText(String.format("Total Amount: %.2f ₽", total));
        });
        
        Scene scene = new Scene(layout, 750, 750);
        dialog.setScene(scene);
        dialog.setResizable(true);
    }
    
    private void setupItemsTable() {
        TableColumn<OrderItem, String> colName = new TableColumn<>("Product");
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colName.setPrefWidth(250);
        
        TableColumn<OrderItem, Integer> colQty = new TableColumn<>("Qty");
        colQty.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        colQty.setPrefWidth(80);
        
        TableColumn<OrderItem, Double> colPrice = new TableColumn<>("Unit Price");
        colPrice.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        colPrice.setPrefWidth(100);
        
        TableColumn<OrderItem, Double> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleDoubleProperty(
                data.getValue().getQuantity() * data.getValue().getPrice()
            ).asObject()
        );
        colTotal.setPrefWidth(120);
        
        // Nút xóa
        TableColumn<OrderItem, Void> colAction = new TableColumn<>("Action");
        colAction.setCellFactory(param -> new TableCell<OrderItem, Void>() {
            private final Button btn = new Button("Remove");
            {
                btn.setStyle("-fx-text-fill: #f44336; -fx-cursor: hand; -fx-font-weight: bold;");
                btn.setOnAction(event -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    if (confirm("Remove \"" + item.getName() + "\" from order?")) {
                        // Hoàn trả số lượng vào kho
                        Product product = ProductDAO.getProductById(item.getProductId());
                        if (product != null) {
                            ProductDAO.updateStock(product.getId(), 
                                product.getStockQty() + item.getQuantity());
                        }
                        items.remove(item);
                        showAlert("Product removed from order and stock restored!");
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        colAction.setPrefWidth(100);
        
        itemsTable.getColumns().addAll(colName, colQty, colPrice, colTotal, colAction);
    }
    
    private void addProductToOrder(Product product) {
        try {
            int qty = Integer.parseInt(txtQuantity.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
            
            if (qty > product.getStockQty()) {
                showAlert("Insufficient stock!\n\nAvailable: " + product.getStockQty() + " units\nRequested: " + qty + " units");
                return;
            }
            
            // Kiểm tra sản phẩm đã có trong đơn chưa
            OrderItem existing = null;
            for (OrderItem item : items) {
                if (item.getProductId() == product.getId()) {
                    existing = item;
                    break;
                }
            }
            
            if (existing != null) {
                // Cập nhật số lượng
                int newQty = existing.getQuantity() + qty;
                if (newQty > product.getStockQty()) {
                    showAlert("Insufficient stock!\n\nAvailable: " + product.getStockQty() + " units\nCurrent in order: " + existing.getQuantity() + " units\nRequested additional: " + qty + " units");
                    return;
                }
                existing.setQuantity(newQty);
                itemsTable.refresh();
                showAlert("Updated quantity for \"" + product.getName() + "\" to " + newQty + " units");
            } else {
                // Thêm mới
                items.add(new OrderItem(
                    product.getId(),
                    product.getName(),
                    qty,
                    product.getPrice()
                ));
                showAlert("Added \"" + product.getName() + "\" to order!");
            }
            
            // Trừ số lượng trong kho NGAY LẬP TỨC
            ProductDAO.updateStock(product.getId(), product.getStockQty() - qty);
            
            // Reset
            txtSearchProduct.clear();
            txtQuantity.setText("1");
            txtSearchProduct.requestFocus();
            
            // Refresh danh sách sản phẩm để cập nhật stock
            refreshProductList();
            
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid quantity (positive number)!");
        }
    }
    
    private void refreshProductList() {
        allProducts.setAll(ProductDAO.getAllProducts());
    }
    
    private void saveOrder() {
        // 1. Validation khách hàng
        String customerInput = txtCustomer.getText().trim();
        if (customerInput.isEmpty()) {
            showAlert("Please enter customer phone or email!");
            txtCustomer.requestFocus();
            return;
        }
        
        // 2. Tìm khách hàng theo phone hoặc email
        Customer customer = CustomerDAO.getCustomerByPhoneOrEmail(customerInput);
        
        System.out.println("Customer found: " + (customer != null));
        if (customer != null) {
            System.out.println("Customer ID: " + customer.getId());
            System.out.println("Customer Name: " + customer.getFullName());
        }
        
        if (customer == null) {
            showAlert("Customer not found!\n\nPhone/Email: " + customerInput + 
                      "\n\nPlease add this customer first in the Customers tab.");
            return;
        }
        
        // ✅ THÊM KIỂM TRA ID
        if (customer.getId() <= 0) {
            showAlert("Invalid customer ID: " + customer.getId() + 
                      "\n\nPlease check customer data in database.");
            return;
        }
        
        // 3. Validation ngày
        LocalDate date = dpDate.getValue();
        if (date == null) {
            showAlert("Please select an order date!");
            return;
        }
        
        // 4. Validation sản phẩm
        if (items.isEmpty()) {
            showAlert("Please add at least one product to the order!");
            return;
        }
        
        // 5. Tính tổng tiền
        double totalAmount = items.stream()
            .mapToDouble(item -> item.getQuantity() * item.getPrice())
            .sum();
        
        try {
            // 6. Tạo đơn hàng (ID = 0, database sẽ tự sinh)
        	Order newOrder = new Order(
        		    0,                      // id (DB tự sinh)
        		    customer.getId(),       // ✅ customerId (bắt buộc phải có)
        		    customer.getFullName(), // customer name
        		    date,                   // order date
        		    currentUserId,          // created by
        		    totalAmount             // total amount
        		);
            
            // 7. Thêm vào database (method này sẽ cập nhật ID cho newOrder)
            OrderDAO.addOrder(newOrder);
            
            // 8. Kiểm tra ID đã được sinh chưa
            if (newOrder.getId() <= 0) {
                showAlert("Error: Failed to generate Order ID!");
                return;
            }
            
            InvoiceDAO.createInvoice(new Invoice(
                    newOrder.getId(),
                    LocalDate.now(),
                    currentUserId,
                    "UNPAID"
                ));
            
            for (OrderItem item : items) {
                TrackingDAO.addTracking(
                    newOrder.getId(),           // order_id (đã được sinh)
                    item.getProductId(),        // product_id
                    item.getQuantity(),         // quantity
                    "PENDING"                   // status
                );
            }
            
            // 10. Thông báo thành công
            showAlert("Order created successfully!\n\n" +
                    "Order ID: " + newOrder.getId() + "\n" +
                    "Payment Status: UNPAID\n" +  
                    "Total Amount: " + String.format("%.2f ₽", totalAmount));
            
            // 11. Đóng dialog
            dialog.close();
            
        } catch (Exception e) {
            showAlert("Error saving order: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean confirm(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.initOwner(dialog);
        return alert.showAndWait().get() == ButtonType.OK;
    }
    
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.initOwner(dialog);
        alert.showAndWait();
    }
    
    public void show() {
        dialog.showAndWait();
    }
}