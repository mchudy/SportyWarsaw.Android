package pl.sportywarsaw.models;

import java.util.Date;

import pl.sportywarsaw.enums.SportType;

public class MeetingModel {
    private int id;
    private String title;
    private int maxParticipants;
    private double cost;
    private Date startTime;
    private Date endTime;
    private SportType sportType;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public double getCost() {
        return cost;
    }

    public Date getStartTime() {return startTime;}

    public Date getEndTime() {
        return endTime;
    }

    public SportType getSportType() {
        return sportType;
    }
}
