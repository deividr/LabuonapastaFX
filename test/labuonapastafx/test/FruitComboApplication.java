package labuonapastafx.test;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/** Main application class for fruit combo fxml demo application */
public class FruitComboApplication extends Application {
  public static void main(String[] args) { launch(args); }
  @Override public void start(Stage stage) throws IOException {
    stage.setTitle("Choices");
    stage.getIcons().add(new Image("http://files.softicons.com/download/application-icons/pixelophilia-icons-by-omercetin/png/32/apple-green.png"));
    AnchorPane layout = FXMLLoader.load(
      new URL(FruitComboApplication.class.getResource("fruitcombo.fxml").toExternalForm())
    );
    stage.setScene(new Scene(layout));
    stage.show();
  }
}