package com.example.workmanagerimplementation.Models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.example.workmanagerimplementation.Models.Pojo.Sales;
import com.example.workmanagerimplementation.data.DataContract;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Md.harun or rashid on 24,March,2021
 * BABL, Bangladesh,
 */
public class SalesModel {
    ContentResolver contentResolver;

    public SalesModel(ContentResolver contentResolver){
        this.contentResolver=contentResolver;
    }


    public ArrayList<Sales> salesOrder(){
        ArrayList<Sales> sales=new ArrayList<>();
        String[] projection={
                DataContract.SalesEntry.SALES_ORDER_ID,
                DataContract.SalesEntry.SO_ORACLE_ID,
                DataContract.SalesEntry.DEALER_NAME,
                DataContract.SalesEntry.NAME,
                DataContract.SalesEntry.ORDER_DATE,
                DataContract.SalesEntry.ORDER_DATE_TIME,
                DataContract.SalesEntry.DELIVERY_DATE

        };
        Cursor cursor=contentResolver.query(DataContract.SalesEntry.CONTENT_URI,projection,null,null,null);
        Log.e("cursor",new Gson().toJson(cursor));
        if(cursor.moveToFirst()){
            do {
                sales.add(new Sales(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)

                ));
            }while (cursor.moveToNext());
        }
        Log.e("getSales",new Gson().toJson(sales));
        return sales;
    }
}
