package com.example.workmanagerimplementation.Models;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.workmanagerimplementation.Models.Pojo.Employee;
import com.example.workmanagerimplementation.data.DataContract;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Md.harun or rashid on 22,March,2021
 * BABL, Bangladesh,
 */
public class EmployeeModel {
    ContentResolver contentResolver;

    public EmployeeModel(ContentResolver contentResolver){
        this.contentResolver=contentResolver;
    }

    public String insertEmployee(Employee employee){

        ContentValues values=new ContentValues();

        values.put(DataContract.EmployeeEntry.NAME,employee.getName());
        values.put(DataContract.EmployeeEntry.AGE,employee.getAge());
        values.put(DataContract.EmployeeEntry.PHONE,employee.getPhone());
        values.put(DataContract.EmployeeEntry.EMAIL,employee.getEmail());
        values.put(DataContract.EmployeeEntry.COLUMN_ID,employee.getColumn_id());
        values.put(DataContract.EmployeeEntry.IS_SYNCED,employee.getIs_synced());

        contentResolver.insert(DataContract.EmployeeEntry.CONTENT_URI,values);
        return employee.getName();

    }

    public ArrayList<Employee> getEmployeeList(){
        ArrayList<Employee> employees=new ArrayList<>();
        String[] projection={
                DataContract.EmployeeEntry.NAME,
                DataContract.EmployeeEntry.AGE,
                DataContract.EmployeeEntry.PHONE,
                DataContract.EmployeeEntry.EMAIL,
                DataContract.EmployeeEntry.IS_SYNCED
        };

        Cursor cursor=contentResolver.query(DataContract.EmployeeEntry.CONTENT_URI,projection,null,null,null);
        Log.e("cursor",new Gson().toJson(cursor));
        if (cursor.moveToFirst()){
            do {
                employees.add(new Employee(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4)
                ));
            }while (cursor.moveToNext());
        }

        return employees;
    }
}
