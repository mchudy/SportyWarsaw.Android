package net.azurewebsites.sportywarsaw.models;

/**
 * Represents the basic properties of a friendship
 *
 * Created by Jan Kierzkowski on 04.01.2016.
 */

import java.util.Date;


public class FriendshipModel {
    private String InviterId;
    private String FriendId;
    private boolean IsConfirmed;
    private Date CreatedTime;

    public String getInviterId() {
        return InviterId;
    }

    public void setInviterId(String inviterId) {
        InviterId = inviterId;
    }

    public String getFriendId() {
        return FriendId;
    }

    public void setFriendId(String friendId) {
        FriendId = friendId;
    }

    public boolean isConfirmed() {
        return IsConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        IsConfirmed = isConfirmed;
    }

    public Date getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Date createdTime) {
        CreatedTime = createdTime;
    }
}
