package ru.nsu.database.airportclient.gui.infonodes;

import javafx.scene.control.CheckBox;

public class CheckBoxInfo implements InfoNode {

    private final CheckBox checkBox;

    public CheckBoxInfo(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    @Override
    public String getInfo() {
        return checkBox.isSelected() ? "T" : "F";
    }

    @Override
    public void setInfo(String info) {
        checkBox.setSelected(info.equals("T"));
    }
}
