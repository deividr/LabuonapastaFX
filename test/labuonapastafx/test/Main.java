package labuonapastafx.test;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
/*  w  w w. j a va2  s .  c  o  m*/
public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }
  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(new Group(), 450, 250);
    ComboBox<String> myComboBox = new ComboBox<String>();
    myComboBox.getItems().addAll("A","B","C","D","E");
    myComboBox.setValue("A");
    System.out.println(myComboBox.getValue());
    
    myComboBox.setValue(null);
    
    Group root = (Group) scene.getRoot();
    root.getChildren().add(myComboBox);
    stage.setScene(scene);
    stage.show();
  }
}