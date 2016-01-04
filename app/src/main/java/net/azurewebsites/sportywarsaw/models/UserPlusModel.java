package net.azurewebsites.sportywarsaw.models;

import java.util.List;

/**
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public class UserPlusModel {
    private String firstName;
    private String lastName;
    private byte[] picture;
    private String userName;

    // // TODO: 04.01.2016

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<FriendshipModel> getFriendshipsInitiated() {
        return friendshipsInitiated;
    }

    public void setFriendshipsInitiated(List<FriendshipModel> friendshipsInitiated) {
        this.friendshipsInitiated = friendshipsInitiated;
    }

    public List<FriendshipModel> getFriendshipsRequested() {
        return friendshipsRequested;
    }

    public void setFriendshipsRequested(List<FriendshipModel> friendshipsRequested) {
        this.friendshipsRequested = friendshipsRequested;
    }

    // public IList<MeetingPlusModel> Meetings { get; set; }

    private List<FriendshipModel> friendshipsInitiated;

    private List<FriendshipModel> friendshipsRequested;
}
