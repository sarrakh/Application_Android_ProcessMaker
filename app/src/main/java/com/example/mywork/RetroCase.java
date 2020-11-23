package com.example.mywork;

public class RetroCase {
    private String tas_uid;
    private String pro_title;
    private String pro_uid;


    public RetroCase(String tas_uid, String pro_title, String pro_uid) {
        this.tas_uid = tas_uid;
        this.pro_title = pro_title;
        this.pro_uid = pro_uid;
    }

    public String getTas_uid() {
        return tas_uid;
    }

    public void setTas_uid(String tas_uid) {
        this.tas_uid = tas_uid;
    }

    public String getPro_title() {
        return pro_title;
    }

    public void setPro_title(String pro_title) {
        this.pro_title = pro_title;
    }

    public String getPro_uid() {
        return pro_uid;
    }

    public void setPro_uid(String pro_uid) {
        this.pro_uid = pro_uid;
    }

    @Override
    public String toString() {
        return "RetroCase{" +
                "tas_uid='" + tas_uid + '\'' +
                ", pro_title='" + pro_title + '\'' +
                ", pro_uid='" + pro_uid + '\'' +
                '}';
    }

    public RetroCase() {
    }
}
