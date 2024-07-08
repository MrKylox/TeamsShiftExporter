package edu.ITSolutions.Export.Controller;

import java.io.IOError;
import java.util.*;
import edu.ITSolutions.Export.Shift;

public class ProfileController {

    private int compareShift(Shift shift1, Shift shift2){
        String startTime1 = shift1.getStartTime();
        String startTime2 = shift2.getStartTime();
        // System.out.println(startTime1.compareTo(startTime2)); debugging
        // System.out.println("Function called"); debugging

        return startTime1.compareTo(startTime2);
    }

    public List<Shift> sortList(List<Shift> orginalList){
        List<Shift> sortedShiftList = new ArrayList<>(orginalList);
        System.out.println(sortedShiftList);
        
        int size = sortedShiftList.size();
        try{
            for(int i = 0; i < size - 1; i++){
                for(int j = 0; j < size - i - 1; j++){
                    if(compareShift(sortedShiftList.get(j), sortedShiftList.get(j+1)) > 0){
                        Shift temp = sortedShiftList.get(j);
                        sortedShiftList.set(j,sortedShiftList.get(j+1));
                        sortedShiftList.set(j+1, temp);
                    }
                }
            }
        }
        catch(IOError e){
            e.printStackTrace();
        }
        
        return orginalList;
    }
}
