package edu.ITSolutions.Export;

public class Shift {
    private final String member;
    private final String weekDay;
    private final String startDate;
    private final String startTime;
    private final String endDate;
    private final String endTime;
    private final String group;
    private final String themeColor;

    public Shift(String member, String weekDay, String startDate, String startTime, String endDate, String endTime, String group, String themeColor) {
        this.member = member;
        this.weekDay = weekDay;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.group = group;
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

    public String getGroup() {
        return group;
    }

    public String getThemeColor() {
        return themeColor;
    }
}
