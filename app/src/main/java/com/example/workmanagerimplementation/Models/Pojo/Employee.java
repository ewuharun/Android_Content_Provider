package com.example.workmanagerimplementation.Models.Pojo;

/**
 * Created by Md.harun or rashid on 22,March,2021
 * BABL, Bangladesh,
 */
public class Employee {
    private String name;
    private int age,is_synced,column_id;
    private String phone,email;

    public int getColumn_id() {
        return column_id;
    }

    public void setColumn_id(int column_id) {
        this.column_id = column_id;
    }

    public Employee(String name, int age, String phone, String email,int column_id,int is_synced) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.column_id=column_id;
        this.is_synced=is_synced;
    }

    public Employee(String name, int age, String phone, String email,int is_synced) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.is_synced=is_synced;
    }


    public int getIs_synced() {
        return is_synced;
    }

    public void setIs_synced(int is_synced) {
        this.is_synced = is_synced;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
