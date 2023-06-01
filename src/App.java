import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MPlayer.fxml").openStream());

        primaryStage.setScene(new Scene(root,1200,600));
        primaryStage.setMinWidth(625);
        primaryStage.setMinHeight(180);
        primaryStage.setTitle("Media Player");
        primaryStage.show();
    }
 
 public static void main(String[] args) {
        launch(args);
    }
}