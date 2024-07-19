package edu.ITSolutions.Export.ui;

import javafx.scene.layout.VBox;


public class AllShifts{

    private static VBox vbox;
    private static MemberShiftShower allShiftShower;
    private static MainUI mainUI;

    public AllShifts(){
        allShiftShower = new MemberShiftShower();
        mainUI = new MainUI();
    }

    public VBox createAllShiftsLayout(){
        vbox = new VBox();
        allShiftShower.setShiftList(mainUI.displayAllShifts());
        vbox.getChildren().setAll(allShiftShower);
        allShiftShower.refreshTables();

        return vbox;
    }

    public void display(){
        mainUI.displayAllShifts();
    }
}
