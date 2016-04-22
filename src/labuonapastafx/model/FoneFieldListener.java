package labuonapastafx.model;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class FoneFieldListener implements ChangeListener<String> {

    private final TextField field;
    private String value;

    public FoneFieldListener(TextField field) {
        this.field = field;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

        value = field.getText();
        value = value.replaceAll("[^0-9]", "");
        value = value.replaceAll("([0-9]{2})([0-9]{1,11})$", "($1)$2");
        value = value.replaceAll("([0-9]{4,5})([0-9]{4})", "$1-$2");

        Platform.runLater(() -> {
            field.setText(value);
            // Devido ao incremento dos caracteres das máscaras é necessário que o
            // cursor sempre se posicione no final da string.
            field.positionCaret(field.getText().length());
        });

        field.textProperty().addListener((ObservableValue<? extends String> observableValue,
                                          String oldValue1, String newValue1) -> {
            if (newValue1.length() > 14) {
                field.setText(oldValue1);
            }
        });
    }
}
