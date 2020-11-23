package com.example.mywork;

public class RetroStep {
    private String step_type_obj;
    private String step_uid_obj;
    private String obj_title;
    private String obj_description;

    public RetroStep(String step_type_obj, String step_uid_obj, String obj_title, String obj_description) {
        this.step_type_obj = step_type_obj;
        this.step_uid_obj = step_uid_obj;
        this.obj_title = obj_title;
        this.obj_description = obj_description;
    }

    public String getStep_type_obj() {
        return step_type_obj;
    }

    public void setStep_type_obj(String step_type_obj) {
        this.step_type_obj = step_type_obj;
    }

    public String getStep_uid_obj() {
        return step_uid_obj;
    }

    public void setStep_uid_obj(String step_uid_obj) {
        this.step_uid_obj = step_uid_obj;
    }

    public String getObj_title() {
        return obj_title;
    }

    public void setObj_title(String obj_title) {
        this.obj_title = obj_title;
    }

    public String getObj_description() {
        return obj_description;
    }

    public void setObj_description(String obj_description) {
        this.obj_description = obj_description;
    }

    @Override
    public String toString() {
        return "RetroStep{" +
                "step_type_obj='" + step_type_obj + '\'' +
                ", step_uid_obj='" + step_uid_obj + '\'' +
                ", obj_title='" + obj_title + '\'' +
                ", obj_description='" + obj_description + '\'' +
                '}';
    }
}