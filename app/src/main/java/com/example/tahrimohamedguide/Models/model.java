package com.example.tahrimohamedguide.Models;

public class model {
    String filename, fileurl,modul,teacher;
    int nod,nol,nov;

    public model() {
    }

    public model(String filename, String fileurl,String modul, int nod, int nol, int nov) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.nod = nod;
        this.nol = nol;
        this.nov = nov;
        this.modul=modul;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getModul() {
        return modul;
    }

    public void setModul(String modul) {
        this.modul = modul;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public int getNod() {
        return nod;
    }

    public void setNod(int nod) {
        this.nod = nod;
    }

    public int getNol() {
        return nol;
    }

    public void setNol(int nol) {
        this.nol = nol;
    }

    public int getNov() {
        return nov;
    }

    public void setNov(int nov) {
        this.nov = nov;
    }
}
