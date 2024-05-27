package ru.nsu.database.airportclient.gui.infonodes;

import javafx.scene.control.TextField;

public class TextFieldInfo implements InfoNode{
    private final TextField textField;

    public TextFieldInfo(TextField textField) {
        this.textField = textField;
    }


    @Override
    public String getInfo() {
        return textField.getText();
    }

    @Override
    public void setInfo(String info) {
        textField.setText(info);
    }
}
