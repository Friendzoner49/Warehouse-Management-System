package project1.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project1.dao.InvoiceDAO;
import project1.dao.TrackingDAO;
import project1.model.Order;

public class OrderStatusDialog {
    private Stage dialog;
    private Order order;
    private int currentUserId;
    private ComboBox<String> cbDelivery;
    private ComboBox<String> cbPayment;

    public OrderStatusDialog(Order order, int userId) {
        this.order = order;
        this.currentUserId = userId;
        buildUI();
    }

    private void buildUI() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Cập nhật trạng thái - Đơn #" + order.getId());

        Label lblTitle = new Label("ORDER STATUS UPDATE");
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2a5298;");

        Label lblInfo = new Label("Mã đơn: " + order.getId() + " | Khách hàng: " + order.getCustomerName());
        lblInfo.setStyle("-fx-font-size: 13px;");

        // Trạng thái giao hàng
        Label lblDelivery = new Label("📦 Delivery Status:");
        lblDelivery.setStyle("-fx-font-weight: bold;");
        cbDelivery = new ComboBox<>();
        cbDelivery.getItems().addAll("PENDING", "PROCESSING", "SHIPPED", "COMPLETED", "CANCELLED");
        cbDelivery.setValue(TrackingDAO.getOrderDeliveryStatus(order.getId()));

        // Trạng thái thanh toán
        Label lblPayment = new Label(" Payment Status:");
        lblPayment.setStyle("-fx-font-weight: bold;");
        cbPayment = new ComboBox<>();
        cbPayment.getItems().addAll("NO_INVOICE", "UNPAID", "PAID");
        cbPayment.setValue(InvoiceDAO.getOrderPaymentStatus(order.getId()));

        VBox statusBox = new VBox(15,
            new HBox(10, lblDelivery, cbDelivery),
            new HBox(10, lblPayment, cbPayment)
        );
        statusBox.setPadding(new Insets(15));
        statusBox.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");

        Button btnSave = new Button("💾 Lưu thay đổi");
        Button btnCancel = new Button("❌ Hủy");
        btnSave.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCancel.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold;");

        btnSave.setOnAction(e -> saveStatus());
        btnCancel.setOnAction(e -> dialog.close());

        HBox btnBox = new HBox(15, btnSave, btnCancel);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10));

        VBox layout = new VBox(15, lblTitle, lblInfo, new Separator(), statusBox, btnBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 480, 280);
        dialog.setScene(scene);
    }

    private void saveStatus() {
        String delivery = cbDelivery.getValue();
        String payment = cbPayment.getValue();

        if (delivery == null || payment == null) {
            showAlert("⚠️ Vui lòng chọn đầy đủ trạng thái!");
            return;
        }

        try {
            TrackingDAO.updateOrderDeliveryStatus(order.getId(), delivery);
            InvoiceDAO.updateOrCreateInvoiceStatus(order.getId(), currentUserId, payment);
            showAlert("✅ Cập nhật thành công!");
            dialog.close();
        } catch (Exception e) {
            showAlert("❌ Lỗi: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(msg);
        alert.initOwner(dialog);
        alert.showAndWait();
    }

    public void show() {
        dialog.showAndWait();
    }
}