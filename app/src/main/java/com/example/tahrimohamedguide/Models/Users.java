package com.example.tahrimohamedguide.Models;

public class Users {
    private String  user_id, user_name , user_prename,user_birth,user_adress,user_department,
            user_model,user_email
            ,user_password,imageid
            , speciality;
    private long user_type;

    public Users(String user_id, String user_name, String user_prename, String user_birth, String user_adress, long user_type, String user_department,
                 String user_model,String user_email,String user_password) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_prename = user_prename;
        this.user_birth = user_birth;
        this.user_adress = user_adress;
        this.user_type = user_type;
        this.user_department = user_department;
        this.user_model = user_model;
        this.user_email=user_email;
        this.user_password=user_password;
    }

    public Users() {
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_prename() {
        return user_prename;
    }

    public void setUser_prename(String user_prename) {
        this.user_prename = user_prename;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(String user_birth) {
        this.user_birth = user_birth;
    }

    public String getUser_adress() {
        return user_adress;
    }

    public void setUser_adress(String user_adress) {
        this.user_adress = user_adress;
    }

    public long getUser_type() {
        return user_type;
    }

    public void setUser_type(long user_type) {
        this.user_type = user_type;
    }

    public String getUser_department() {
        return user_department;
    }

    public void setUser_department(String user_department) {
        this.user_department = user_department;
    }

    public String getUser_model() {
        return user_model;
    }

    public void setUser_model(String user_model) {
        this.user_model = user_model;
    }
}
