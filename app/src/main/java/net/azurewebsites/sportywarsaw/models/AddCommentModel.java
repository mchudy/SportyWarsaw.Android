package net.azurewebsites.sportywarsaw.models;

/**
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public class AddCommentModel {
    private int meetingId;
    private String text;

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
