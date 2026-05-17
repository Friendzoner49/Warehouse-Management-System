package project1.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project1.model.User;

public class ManagerView {
    private Stage stage;
    private User user;
    private TabPane tabPane;
    
    public ManagerView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
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
            createOrdersTab(),
            createTrackingTab(),
            createInvoicesTab()
        );
        tabPane.setPrefSize(1000, 700);
        
        StackPane root = new StackPane();
        root.getChildren().addAll(background, tabPane);
        
        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("ORDER MANAGER - " + user.getUsername());
        stage.setScene(scene);
        stage.show();
    }
    
    private Tab createOrdersTab() {
        Tab tab = new Tab("Orders");
        Label label = new Label("Order Management (View/Edit Only)");
        label.setPadding(new Insets(20));
        tab.setContent(label);
        // TODO: Implement order view/edit (no price editing)
        return tab;
    }
    
    private Tab createTrackingTab() {
        Tab tab = new Tab("Order Tracking");
        Label label = new Label("Track Order Status");
        label.setPadding(new Insets(20));
        tab.setContent(label);
        // TODO: Implement order tracking
        return tab;
    }
    
    private Tab createInvoicesTab() {
        Tab tab = new Tab("Invoices");
        Label label = new Label("Generate & View Invoices");
        label.setPadding(new Insets(20));
        tab.setContent(label);
        // TODO: Implement invoice generation
        return tab;
    }
}