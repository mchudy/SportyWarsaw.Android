package net.azurewebsites.sportywarsaw.models;

/**
 * Represents the basic properties of an user
 *
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public class UserModel {
    private String FirstName;
    private String LastName;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
