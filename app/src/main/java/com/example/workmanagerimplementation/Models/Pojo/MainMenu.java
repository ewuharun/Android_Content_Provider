package com.example.workmanagerimplementation.Models.Pojo;

/**
 * Created by Md.harun or rashid on 09,April,2021
 * BABL, Bangladesh,
 */
public class MainMenu {
    String menuTitle;
    String menuLogoUrl;
    Integer menuPosition;
    int listener_id;

    public MainMenu(String menuTitle, String menuLogoUrl, Integer menuPosition, int listener_id) {
        this.menuTitle = menuTitle;
        this.menuLogoUrl = menuLogoUrl;
        this.menuPosition = menuPosition;
        this.listener_id = listener_id;
    }
    public MainMenu(){

    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuLogoUrl() {
        return menuLogoUrl;
    }

    public void setMenuLogoUrl(String menuLogoUrl) {
        this.menuLogoUrl = menuLogoUrl;
    }

    public Integer getMenuPosition() {
        return menuPosition;
    }

    public void setMenuPosition(int menuPosition) {
        this.menuPosition = menuPosition;
    }



    public int getListener_id() {
        return listener_id;
    }

    public void setListener_id(int listener_id) {
        this.listener_id = listener_id;
    }
}
