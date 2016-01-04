package net.azurewebsites.sportywarsaw.models;

/**
 * Represents the basic properties of a friendship
 *
 * Created by Jan Kierzkowski on 04.01.2016.
 */

import java.util.Date;


public class FriendshipModel {
    private String inviterId;
    private String friendId;
    private boolean isConfirmed;
    private Date createdTime;

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        inviterId = inviterId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        friendId = friendId;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        isConfirmed = isConfirmed;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        createdTime = createdTime;
    }
}
