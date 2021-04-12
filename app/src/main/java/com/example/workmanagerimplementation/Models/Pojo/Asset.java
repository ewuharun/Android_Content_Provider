package com.example.workmanagerimplementation.Models.Pojo;

/**
 * Created by Md.harun or rashid on 12,April,2021
 * BABL, Bangladesh,
 */
public class Asset {
    private String color,companyName,projectName,companyLogo;
    private String project_title,babl_logo,babl_title;

    public Asset(){

    }

    public int getColumn_id() {
        return column_id;
    }

    public void setColumn_id(int column_id) {
        this.column_id = column_id;
    }

    private int column_id;

    public Asset(String color, String companyName, String projectName, String companyLogo, String project_title, String babl_logo, String babl_title,int column_id) {
        this.color = color;
        this.companyName = companyName;
        this.projectName = projectName;
        this.companyLogo = companyLogo;
        this.project_title = project_title;
        this.babl_logo = babl_logo;
        this.babl_title = babl_title;
        this.column_id=column_id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    public String getBabl_logo() {
        return babl_logo;
    }

    public void setBabl_logo(String babl_logo) {
        this.babl_logo = babl_logo;
    }

    public String getBabl_title() {
        return babl_title;
    }

    public void setBabl_title(String babl_title) {
        this.babl_title = babl_title;
    }
}
