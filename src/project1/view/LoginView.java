package project1.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project1.dao.UserDAO;
import project1.model.User;

public class LoginView {
    private Stage stage;
    
    public LoginView(Stage stage) {
        this.stage = stage;
    }
    
    public void show() {
        // Tạo background
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/1.jpg"));
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true)
        );
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        
        Label title = new Label("WAREHOUSE MANAGEMENT SYSTEM");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        TextField txtUser = new TextField();
        txtUser.setPromptText("Username");
        
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password");
        
        Button btnLogin = new Button("LOGIN");
        btnLogin.setDefaultButton(true);
        
        btnLogin.setOnAction(e -> {
            String username = txtUser.getText().trim();
            String password = txtPass.getText();
            
            User user = UserDAO.authenticate(username, password);
            
            if (user != null) {
                stage.setTitle("Warehouse System - " + user.getUsername());
                if ("ADMIN".equals(user.getRole())) {
                    new AdminView(stage, user).show();
                } else {
                    new ManagerView(stage, user).show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setContentText("Invalid username or password!");
                alert.showAndWait();
            }
        });
        
        content.getChildren().addAll(title, txtUser, txtPass, btnLogin);
        
        StackPane root = new StackPane();
        root.setBackground(new Background(background));
        root.getChildren().add(content);
        
        Scene scene = new Scene(root, 350, 250);
        stage.setScene(scene);
        stage.show();
    }
}