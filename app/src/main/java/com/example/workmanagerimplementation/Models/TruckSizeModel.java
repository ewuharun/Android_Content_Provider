package com.example.workmanagerimplementation.Models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.example.workmanagerimplementation.Models.Pojo.Truck;
import com.example.workmanagerimplementation.data.DataContract;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Md.harun or rashid on 22,March,2021
 * BABL, Bangladesh,
 */
public class TruckSizeModel {
    ContentResolver contentResolver;

    public TruckSizeModel(ContentResolver contentResolver){
        this.contentResolver=contentResolver;

    }

    public ArrayList<Truck> getTruckNameList(){
        ArrayList<Truck> trucks=new ArrayList<>();
        String[] projection={
                DataContract.TruckSizeEntry.COLUMN_ID,
                DataContract.TruckSizeEntry.NAME
        };
        Cursor cursor=contentResolver.query(DataContract.TruckSizeEntry.CONTENT_URI,projection,null,null,null);
        Log.e("cursor",new Gson().toJson(cursor));
        if(cursor.moveToFirst()){
            do {
                trucks.add(new Truck(cursor.getInt(0),cursor.getString(1)));
            }while (cursor.moveToNext());
        }
        Log.e("getTruckNameList",new Gson().toJson(trucks));
        return trucks;
    }

}
