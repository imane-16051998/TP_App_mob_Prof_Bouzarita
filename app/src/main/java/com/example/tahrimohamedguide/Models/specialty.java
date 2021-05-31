package com.example.tahrimohamedguide.Models;

public class specialty {

    private String Name , Department ;

    public specialty ( ){

    }

    public specialty(String name, String department) {
        Name = name;
        Department = department;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
