package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import project1.view.LoginView;

public class Main extends Application {
	@Override
    public void start(Stage primaryStage) {
        new LoginView(primaryStage).show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
