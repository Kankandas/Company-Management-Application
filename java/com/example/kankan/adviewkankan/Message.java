package com.example.kankan.adviewkankan;

public class Message {
    private String message,name,photo,depertment,id;

    public Message() {
    }

    public Message(String message, String name, String photo, String depertment, String id) {
        this.message = message;
        this.name = name;
        this.photo = photo;
        this.depertment = depertment;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDepertment() {
        return depertment;
    }

    public void setDepertment(String depertment) {
        this.depertment = depertment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
