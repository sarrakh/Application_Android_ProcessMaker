package com.example.mywork;

public class RetroForm {
    private String dyn_title;
    private String dyn_content;

    public RetroForm(String dyn_title, String dyn_content) {
        this.dyn_title = dyn_title;
        this.dyn_content = dyn_content;
    }

    public String getDyn_title() {
        return dyn_title;
    }

    public void setDyn_title(String dyn_title) {
        this.dyn_title = dyn_title;
    }

    public String getDyn_content() {
        return dyn_content;
    }

    public void setDyn_content(String dyn_content) {
        this.dyn_content = dyn_content;
    }

    @Override
    public String toString() {
        return "RetroForm{" +
                "dyn_title='" + dyn_title + '\'' +
                ", dyn_content='" + dyn_content + '\'' +
                '}';
    }

    public RetroForm() {
    }
}
