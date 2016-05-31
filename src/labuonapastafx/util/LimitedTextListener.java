package labuonapastafx.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class LimitedTextListener implements ChangeListener<String> {

    private final TextField field;
    private final int maxLength;

    public LimitedTextListener(TextField field, int maxLength) {
        this.field = field;
        this.maxLength = maxLength;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        try {
            if (newValue.length() > maxLength) {
                field.setText(oldValue);
            } else {
                field.setText(newValue.replaceAll("[^a-zA-Z0-9 ]", ""));
            }
        } catch (Exception e) {
            field.setText(oldValue);
        }

    }

}
