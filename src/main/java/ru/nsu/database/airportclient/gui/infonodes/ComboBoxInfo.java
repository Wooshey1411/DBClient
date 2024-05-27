package ru.nsu.database.airportclient.gui.infonodes;

import javafx.scene.control.ComboBox;

public class ComboBoxInfo implements InfoNode{

    private final ComboBox<String> comboBox;

    public ComboBoxInfo(ComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public String getInfo() {
        return comboBox.getValue();
    }

    @Override
    public void setInfo(String info) {
        comboBox.setValue(info);
    }
}
