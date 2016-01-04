package net.azurewebsites.sportywarsaw.models;

import net.azurewebsites.sportywarsaw.enums.SportType;

import java.util.Date;

/**
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public class MeetingPlusModel {
    private int id;
    private String title;
    private int maxParticipants;
    private double cost;
    private Date startTime;
    private Date endTime;
    private SportType sportType;
    private String description;
    private String OrganizerName;
    private SportFacilityPlusModel SportsFacility;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    public String getOrganizerName() {
        return OrganizerName;
    }

    public void setOrganizerName(String organizerName) {
        OrganizerName = organizerName;
    }

    public SportFacilityPlusModel getSportsFacility() {
        return SportsFacility;
    }

    public void setSportsFacility(SportFacilityPlusModel sportsFacility) {
        SportsFacility = sportsFacility;
    }
}
