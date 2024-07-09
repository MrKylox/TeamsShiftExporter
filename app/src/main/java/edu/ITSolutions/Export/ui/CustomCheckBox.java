package edu.ITSolutions.Export.ui;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class CustomCheckBox extends HBox{
    private final CheckBox checkBox;

    public CustomCheckBox(){
        this.checkBox = new CheckBox();
    }

    public CheckBox getCheckBox(){
        return this.checkBox;
    }

    public boolean isSelected(){
        return this.checkBox.isSelected();
    }

    public void setSelected(boolean selected){
        this.checkBox.setSelected(selected);
    }


}
