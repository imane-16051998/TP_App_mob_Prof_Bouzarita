package com.example.tahrimohamedguide.Models;

public class Course {

        private String Name , Department ,Speciality ,Teacher;

    public Course(String name, String Speciality,String Teacher) {
       this.Name = name;
        this.Speciality = Speciality;
        this.Teacher=Teacher;
    }

    public Course() {
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
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
