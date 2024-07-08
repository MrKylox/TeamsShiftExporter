package edu.ITSolutions.Export;

public class Shift {
    private final String member;
    private String weekDay;
    private String startDate;
    private final String startTime;
    private String endDate;
    private final String endTime;
    private String position;
    private String email;
    private String group;
    private String color;
    private String themeColor;
    private String season;

    public Shift(String member, String weekDay, String startTime, String endTime, String position, String season){
        this.member = member;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.position = position;
        this.season = season;
    }

    public Shift(String member, String email, String group, String startDate, String startTime, String endDate, String endTime, String themeColor){
        this.member = member;
        this.email = email;
        this.group = group;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.themeColor = themeColor;
    }

    public String getMember() {
        return member;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPosition(){
        return this.position;
    }

    public String getEmail(){
        return this.email;
    }

    public String getThemeColor(){
        return this.themeColor;
    }

    public String getGroup(){
        if(position.equals("Lead Student Consultant")){
            this.group = "Lead Consultants";
        }
        else{
            this.group = "Senior and Student Consultants";
        }
        
        return this.group;
    }

    public String getColor(){
        if(position.equals("Student Consultant")){
            this.color = "3. Green";
        }

        if(position.equals("Senior Student Consultant")){
            this.color = "6. Yellow";
        }

        if(position.equals("Lead Student Consultant")){
            this.color = "12. Dark Yellow";
        }

        return this.color;
    }

    public String getSeason(){
        return this.season;
    }
}
