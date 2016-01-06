package net.azurewebsites.sportywarsaw.models;

import java.util.Date;
/**
 * Represents the basic properties of a single comment
 *
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public class CommentModel {

    private int id;
    private String text;
    private Date date;
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public java.util.Date getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }
}
