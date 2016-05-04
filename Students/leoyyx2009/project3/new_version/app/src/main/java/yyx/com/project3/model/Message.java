package yyx.com.project3.model;

import java.io.Serializable;

import yyx.com.project3.model.*;
import yyx.com.project3.model.User;

public class Message implements Serializable {
    String id, message, createdAt;
    yyx.com.project3.model.User user;

    public Message() {
    }

    public Message(String id, String message, String createdAt, yyx.com.project3.model.User user) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public yyx.com.project3.model.User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
