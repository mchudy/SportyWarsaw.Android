package net.azurewebsites.sportywarsaw.models;

/**
 * Represents the basic properties of an user
 *
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public class UserModel {
    private String firstName;
    private String lastName;
    private String username;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
