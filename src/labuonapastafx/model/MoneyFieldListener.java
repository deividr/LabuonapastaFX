package labuonapastafx.model;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class MoneyFieldListener implements ChangeListener<String> {

    private final TextField field;
    private String value;

    public MoneyFieldListener(TextField field) {
        this.field = field;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

        value = field.getText();
        value = value.replaceAll("[^0-9]", "");
        value = value.replaceAll("([0-9])([0-9]{11})$", "$1.$2");
        value = value.replaceAll("([0-9])([0-9]{8})$", "$1.$2");
        value = value.replaceAll("([0-9])([0-9]{5})$", "$1.$2");
        value = value.replaceAll("([0-9])([0-9]{2})$", "$1,$2");

        Platform.runLater(() -> {
            field.setText(value);
            // Devido ao incremento dos caracteres das máscaras é necessário que o
            // cursor sempre se posicione no final da string.
            field.positionCaret(field.getText().length());
        });

        field.textProperty().addListener((ObservableValue<? extends String> observableValue,
                                          String oldValue1, String newValue1) -> {
            if (newValue1.length() > 13)
                field.setText(oldValue1);
        });

        field.focusedProperty().addListener((ObservableValue<? extends Boolean> observableValue,
                                             Boolean aBoolean, Boolean fieldChange) -> {
            if (!fieldChange) {
                final int length = field.getText().length();
                if (length > 0 && length < 3)
                    field.setText(field.getText() + "00");
            }
        });

    }

}
