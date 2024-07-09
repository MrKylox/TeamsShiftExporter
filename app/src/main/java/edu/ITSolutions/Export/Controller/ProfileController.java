package edu.ITSolutions.Export.Controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Hashtable;

import edu.ITSolutions.Export.Shift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProfileController {

    //compares shift values based on start time of shift
    private int compareShift(Shift shift1, Shift shift2){
        String startTime1 = shift1.getStartTime();
        String startTime2 = shift2.getStartTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");//formatting to convert to localtime type
        LocalTime startTLocalTime1 = LocalTime.parse(startTime1.toUpperCase(),formatter);
        LocalTime startLocalTime2 = LocalTime.parse(startTime2.toUpperCase(),formatter);

        if(startTLocalTime1.isBefore(startLocalTime2)){
            // System.out.println("Time comparison:"+startTime1+" is before " + startLocalTime2);
            return -1;
        }
        else if(startTLocalTime1.isAfter(startLocalTime2)){
            // System.out.println("Time comparison: "+startTime1+" is after " + startLocalTime2);

            return 1;
        }
        else{
            // System.out.println("Time comparison:"+startTime1+" is equal to " + startLocalTime2);

            return 0;
        }
    }

    //compares days
    private int compareDays(String day1, String day2){

        //day dictionary
        Dictionary<String,Integer> dayDict = new Hashtable<>();
        dayDict.put("MONDAY",0);
        dayDict.put("TUESDAY",1);
        dayDict.put("WEDNESDAY",2);
        dayDict.put("THURSDAY",3);
        dayDict.put("FRIDAY",4);
        dayDict.put("SATURDAY",5);
        dayDict.put("SUNDAY",6);

        if(dayDict.get(day1) > dayDict.get(day2)){
            // System.out.println("Day comparison " + day1 + " is after "+ day2);
            return 1;
        }
        else if(dayDict.get(day1) == dayDict.get(day2)){
            // System.out.println("Day comparison " + day1 + " is eqaul "+ day2);
            return 0;
        }
        else{
            // System.out.println("Day comparison " + day1 + " is before "+ day2);
            return -1;
        }
    }

    //sorts list based on comparing days and start time of each shift
    public ObservableList<Shift> sortList(ObservableList<Shift> orginalList){
        ObservableList<Shift> sortedShiftList = FXCollections.observableArrayList(orginalList);//copy of orginalList...Needs to be opitimized for space complexity

        // System.out.println(sortedShiftList); debugging
        
        int size = sortedShiftList.size();
        
        for(int i = 0; i < size - 1; i++){ 
            for(int j = 0; j < size - i - 1; j++){
                Shift shift1 = sortedShiftList.get(j);
                Shift shift2 = sortedShiftList.get(j+1);
                String day1 = shift1.getWeekDay();
                String day2 = shift2.getWeekDay();

                int dayComparison = compareDays(day1,day2);//comparing days
                if(dayComparison == 0){
                    //comparing shift to determine if swap is needed
                    if(compareShift(shift1,shift2) > 0){
                        Shift temp = sortedShiftList.get(j);
                        sortedShiftList.set(j,sortedShiftList.get(j+1));
                        sortedShiftList.set(j+1, temp);
                    }
                }
                else if(dayComparison > 0){
                    //Swap if day1 is after day2
                    Shift temp = sortedShiftList.get(j);
                    sortedShiftList.set(j, sortedShiftList.get(j+1));
                    sortedShiftList.set(j+1,temp);
                }
            }
        }
        
        return sortedShiftList;//return sorted list
    }
}
