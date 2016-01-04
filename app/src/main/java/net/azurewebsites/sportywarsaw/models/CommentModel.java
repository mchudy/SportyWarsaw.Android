package net.azurewebsites.sportywarsaw.models;

import java.util.Date;
/**
 * Represents the basic properties of a single comment
 *
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public class CommentModel {

    private int Id;
    private String Text;
    private Date Date;
    private String UserId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }
}
