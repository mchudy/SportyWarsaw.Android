package net.azurewebsites.sportywarsaw.models;

import net.azurewebsites.sportywarsaw.enums.SportType;

import java.text.DecimalFormat;
import java.util.Date;
/**
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public class AddMeetingModel {
    private String title;
    private int maxParticipants;
    private Date startTime;
    //TODO check if after StartTime
    private Date endTime;
    private SportType sportType;
    private double cost;
    private String description;
    private int sportsFacilityId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSportsFacilityId() {
        return sportsFacilityId;
    }

    public void setSportsFacilityId(int sportsFacilityId) {
        this.sportsFacilityId = sportsFacilityId;
    }
}
