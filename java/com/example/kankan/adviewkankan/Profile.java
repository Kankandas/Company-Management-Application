package com.example.kankan.adviewkankan;

public class Profile {
    private String name,age,position,profilePhoto,department,user_Id,email;

    public Profile() {
    }

    public Profile(String name, String age, String position, String profilePhoto, String department, String user_Id, String email) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.profilePhoto = profilePhoto;
        this.department = department;
        this.user_Id = user_Id;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
