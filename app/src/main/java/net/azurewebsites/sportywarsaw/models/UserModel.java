package net.azurewebsites.sportywarsaw.models;

/**
 * Represents the basic properties of an user
 *
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public class UserModel {
    private String firstName;
    private String lastName;
    private String userName;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
