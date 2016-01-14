package pl.sportywarsaw.models;

/**
 * Represents the basic properties of a friendship
 *
 * Created by Jan Kierzkowski on 04.01.2016.
 */

import java.util.Date;


public class FriendshipModel {
    private String inviterUsername;
    private String friendUsername;
    private boolean isConfirmed;
    private Date createdTime;

    public String getInviterUsername() {
        return inviterUsername;
    }

    public void setInviterUsername(String inviterUsername) {
        this.inviterUsername = inviterUsername;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
