package com.example.workmanagerimplementation.Models;

import android.content.Context;
import android.util.Log;

import com.example.workmanagerimplementation.Models.Pojo.Asset;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.JsonParser;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Md.harun or rashid on 12,April,2021
 * BABL, Bangladesh,
 */
public class AssetModel {
    private Context context;

    public AssetModel() {
    }

    public Asset getAssetData(String data){
        Asset asset=new Asset();

        Log.e("assetListData",data);

        HashMap<String,String> map=JsonParser.getAssetData(data);

        String column_id,babl_logo,babl_title,color,company_logo;
        String company_name,project_title,project_name;

        column_id=map.get("column_id");
        babl_logo=map.get("babl_logo");
        babl_title=map.get("babl_title");
        color=map.get("color");
        company_logo=map.get("company_logo");
        company_name=map.get("company_name");
        project_title=map.get("project_title");
        project_name=map.get("project_name");

        asset.setColor(color);
        asset.setBabl_logo(babl_logo);
        asset.setBabl_title(babl_title);
        asset.setColumn_id(Integer.valueOf(column_id));
        asset.setProject_title(project_title);
        asset.setCompanyLogo(company_logo);
        asset.setProjectName(project_name);
        asset.setCompanyName(company_name);


        Log.e("assssss",new Gson().toJson(asset));

        return asset;
    }









}
