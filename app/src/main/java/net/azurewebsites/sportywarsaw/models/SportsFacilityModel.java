package net.azurewebsites.sportywarsaw.models;

/**
 * Represents the basic properties of a sports facility
 *
 * @author Marcin Chudy
 */
public class SportsFacilityModel {
    private int id;
    private String description;
    private String street;
    private String number;
    private String district;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
