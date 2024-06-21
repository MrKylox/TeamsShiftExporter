package edu.ITSolutions.Export;

public class Shift {
    private final String startDate;
    private final String startTime;
    private final String endDate;
    private final String endTime;
    private final String group;
    private final String themeColor;
    private final String memberName;

    public Shift(String startDate, String startTime, String endDate, String endTime, String group, String themeColor, String memberName) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.group = group;
        this.themeColor = themeColor;
        this.memberName = memberName;
    }

    public String getStartDate(){
        return this.startDate;
    }

    public String getEndDate(){
        return this.endDate;
    }

    public String getStartTime(){
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }

    public String getGroup(){
        return this.group;
    }

    public String getThemeColor(){
        return this.themeColor;
    }

    public String getMember() {
        return this.memberName;
    }

}
