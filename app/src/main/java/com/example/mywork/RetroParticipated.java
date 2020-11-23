package com.example.mywork;

public class RetroParticipated {
    String app_title;
    String app_pro_title;
    String app_tas_title;
    String app_create_date;
    String app_current_user;
    String app_status;

    public RetroParticipated(String app_title, String app_pro_title, String app_tas_title, String app_create_date, String app_current_user, String app_status) {
        this.app_title = app_title;
        this.app_pro_title = app_pro_title;
        this.app_tas_title = app_tas_title;
        this.app_create_date = app_create_date;
        this.app_current_user = app_current_user;
        this.app_status = app_status;
    }



    public String getApp_title() {
        return app_title;
    }

    public void setApp_title(String app_title) {
        this.app_title = app_title;
    }

    public String getApp_pro_title() {
        return app_pro_title;
    }

    public void setApp_pro_title(String app_pro_title) {
        this.app_pro_title = app_pro_title;
    }

    public String getApp_tas_title() {
        return app_tas_title;
    }

    public void setApp_tas_title(String app_tas_title) {
        this.app_tas_title = app_tas_title;
    }

    public String getApp_create_date() {
        return app_create_date;
    }

    public void setApp_create_date(String app_create_date) {
        this.app_create_date = app_create_date;
    }

    public String getApp_current_user() {
        return app_current_user;
    }

    public void setApp_current_user(String app_current_user) {
        this.app_current_user = app_current_user;
    }

    public String getApp_status() {
        return app_status;
    }

    public void setApp_status(String app_status) {
        this.app_status = app_status;
    }

    @Override
    public String toString() {
        return "RetroParticipated{" +
                "app_title='" + app_title + '\'' +
                ", app_pro_title='" + app_pro_title + '\'' +
                ", app_tas_title='" + app_tas_title + '\'' +
                ", app_create_date='" + app_create_date + '\'' +
                ", app_current_user='" + app_current_user + '\'' +
                ", app_status='" + app_status + '\'' +
                '}';
    }
}
