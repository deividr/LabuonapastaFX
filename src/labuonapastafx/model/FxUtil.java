package labuonapastafx.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FxUtil {

    public enum AutoCompleteMode {
        STARTS_WITH, CONTAINING,;
    }

    public static <T> void autoCompleteComboBox(ComboBox<T> comboBox, AutoCompleteMode mode) {
        ObservableList<T> data = comboBox.getItems();

        comboBox.setEditable(true);
        comboBox.getEditor().focusedProperty().addListener(observable -> {
            if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
                comboBox.getEditor().setText(null);
            }
        });
        comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                if (null != event.getCode()) switch (event.getCode()) {
                    case UP:
                        caretPos = -1;
                        moveCaret(comboBox.getEditor().getText().length());
                        return;
                    case DOWN:
                        if (!comboBox.isShowing()) {
                            comboBox.show();
                        }
                        caretPos = -1;
                        moveCaret(comboBox.getEditor().getText().length());
                        return;
                    case BACK_SPACE:
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                        break;
                    case DELETE:
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                        break;
                    default:
                        break;
                }

                if (event.getCode() == KeyCode.RIGHT 
                        || event.getCode() == KeyCode.LEFT 
                        || event.getCode().equals(KeyCode.SHIFT) 
                        || event.getCode().equals(KeyCode.CONTROL)
                        || event.isControlDown() 
                        || event.getCode() == KeyCode.HOME
                        || event.getCode() == KeyCode.END 
                        || event.getCode() == KeyCode.TAB) {
                    return;
                }

                ObservableList<T> list = FXCollections.observableArrayList();
                data.stream().forEach((aData) -> {
                    if (mode.equals(AutoCompleteMode.STARTS_WITH) 
                            && aData.toString().toLowerCase().startsWith(comboBox.getEditor()
                                    .getText().toLowerCase())) {
                        list.add(aData);
                    } else if (mode.equals(AutoCompleteMode.CONTAINING) 
                            && aData.toString().toLowerCase().contains(comboBox.getEditor()
                                    .getText().toLowerCase())) {
                        list.add(aData);
                    }
                });
                String t = comboBox.getEditor().getText();

                comboBox.setItems(list);
                comboBox.getEditor().setText(t);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(t.length());
                if (!list.isEmpty()) {
                    comboBox.show();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    comboBox.getEditor().positionCaret(textLength);
                } else {
                    comboBox.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
    }

    public static <T> T getComboBoxValue(ComboBox<T> comboBox) {
        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
        }
    }
}