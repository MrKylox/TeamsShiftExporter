package edu.ITSolutions.Export.ui;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import edu.ITSolutions.Export.Shift;
import edu.ITSolutions.Export.util.ProfilesUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class AllShifts{

    private static VBox vbox;
    private static HBox hbox;
    private static AllShiftShower allShiftShower;
    private static ProfilesUtil profilesUtil;


    public AllShifts(){
        allShiftShower = new AllShiftShower();
        try {
            profilesUtil = new ProfilesUtil();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox createAllShiftsLayout(){
        vbox = new VBox();
        hbox = new HBox();
        allShiftShower.setAllShiftList(getAllShifts());
        hbox.getChildren().setAll(allShiftShower);
        allShiftShower.refreshTables();
        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().setAll(hbox);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    public ObservableList<Shift> getAllShifts(){
        Sheet memberShiftSheet = profilesUtil.getMemeberSheet();
        ObservableList<String> allMemberNameList = FXCollections.observableArrayList();
        ObservableList<Shift> allMemberShiftList = FXCollections.observableArrayList();

        for(Row row : memberShiftSheet){
            if (row.getRowNum() == 0) continue; // Skip header row
            String memberName = row.getCell(0).getStringCellValue(); //get the memberName
            if(!allMemberNameList.contains(memberName)){
                allMemberNameList.add(memberName);
            }
        }
        
        for(String name : allMemberNameList){
            allMemberShiftList.addAll(profilesUtil.getProfileShifts(name));
        }

        allShiftShower.setAllShiftList(allMemberShiftList);
        hbox.getChildren().setAll(allShiftShower);



        return allMemberShiftList;
    }
}
