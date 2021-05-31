package com.example.tahrimohamedguide.Models;

public class DownModel {
    String Name,Link,Modul;

    public String getModul() {
        return Modul;
    }

    public void setModul(String modul) {
        Modul = modul;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
    public DownModel(String Name, String Link){
        this.Link=Link;
        this.Name=Name;

    }

}

