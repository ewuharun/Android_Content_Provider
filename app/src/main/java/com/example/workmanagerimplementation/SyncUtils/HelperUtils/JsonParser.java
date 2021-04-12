package com.example.workmanagerimplementation.SyncUtils.HelperUtils;

import android.content.ContentValues;
import android.util.Log;

import com.example.workmanagerimplementation.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Md.harun or rashid on 21,March,2021
 * BABL, Bangladesh,
 */
public class JsonParser {

    //This function is responsible for getting all tableName from jsonData String
    public static ArrayList<String> ifValidJsonGetTable(String jsonData){
        ArrayList<String> tableName=new ArrayList<>();
        try{
            JSONObject data=new JSONObject(jsonData);
            Log.e("json",new Gson().toJson(data));
            if (!data.has("status")) {
                for (int i = 0; i < data.length(); i++) {
                    tableName.add(data.names().getString(i));
                }
            } else {
                Log.e("RequestedResult", jsonData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tableName;
    }



    public static  HashMap<String, String> getAssetData(String data){
        HashMap<String,String> dataMap=new HashMap<>();
        if(data!=null){
            try{
                JSONObject jsonObject=new JSONObject(data);
                JSONArray jsonArray=jsonObject.getJSONObject("Login_Info_Data").getJSONArray("data");
                Log.v("Data Row Length : ", " : " + jsonArray.length());

                for(int i=0;i<jsonArray.length();++i){
                    JSONObject dataRows=jsonArray.getJSONObject(i);

                    Iterator iterator=dataRows.keys();

                    String val = null;

                    for(int k=0;iterator.hasNext();k++){
                        String column_name=iterator.next().toString();
                        String column_value=dataRows.getString(column_name);
                        Log.e("content",column_value);
                        val=column_value;
                        dataMap.put(column_name,column_value);
                    }

                }



            }catch (JSONException jsonException){
                jsonException.printStackTrace();
            }
        }

        Log.e("DataHas",new Gson().toJson(dataMap));

        return dataMap;
    }




    public static HashMap<String, ContentValues> getColIdAndValues(String dataString, String tableName) {
        HashMap<String, ContentValues> valueList = new HashMap<>();
        if (dataString != null) {
            try {
                JSONObject data = new JSONObject(dataString);
//                String action_name = data.getJSONObject(tableName).getString("action");
//                Log.e("Json Obtain Action : ", action_name);
                JSONArray dataArray = data.getJSONObject(tableName).getJSONArray("data");
                Log.v("Data Row Length : ", " : " + dataArray.length());
                for (int i = 0; i < dataArray.length(); ++i) {
                    JSONObject dataRows = dataArray.getJSONObject(i);
                    Iterator iterator = dataRows.keys();
                    String column_id = dataArray.getJSONObject(i).getString("column_id");

                    ContentValues mNewValues = new ContentValues();
                    for (int k = 0; iterator.hasNext(); k++) {
                        String single_column_name = (String) iterator.next();
                        String column_val = dataRows.getString(single_column_name).toString();
                        mNewValues.put(single_column_name, column_val);
                    }
                    valueList.put(column_id , mNewValues);
                }

            } catch (JSONException var21) {
                var21.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn\'t get any data from the url");
        }
        return valueList;
    }

    public static HashMap<String, ContentValues> getColIdAndValuesForTestData(String dataString, String tableName) {
        HashMap<String, ContentValues> valueList = new HashMap<>();
        if (dataString != null) {
            try {
                JSONObject data = new JSONObject(dataString);
//                String action_name = data.getJSONObject(tableName).getString("action");
//                Log.e("Json Obtain Action : ", action_name);
                JSONArray dataArray = data.getJSONObject(tableName).getJSONArray("data");
                Log.v("Data Row Length : ", " : " + dataArray.length());
                for (int i = 0; i < dataArray.length(); ++i) {
                    JSONObject dataRows = dataArray.getJSONObject(i);
                    Iterator iterator = dataRows.keys();
                    String column_id = dataArray.getJSONObject(i).getString("sales_order_id");

                    ContentValues mNewValues = new ContentValues();
                    for (int k = 0; iterator.hasNext(); k++) {
                        String single_column_name = (String) iterator.next();
                        String column_val = dataRows.getString(single_column_name).toString();
                        mNewValues.put(single_column_name, column_val);
                    }
                    valueList.put(column_id , mNewValues);
                }

            } catch (JSONException var21) {
                var21.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn\'t get any data from the url");
        }
        return valueList;
    }

    public static String ifValidJSONGetColumnId(String dataJson) {
        String columnId = null;
        try {
            JSONObject data = new JSONObject(dataJson);
            columnId = data.getString("column_id");
        } catch (JSONException ex) {
            Log.e("not Vailid JSON", dataJson);
            columnId = null;
        }
        return columnId;
    }

}
