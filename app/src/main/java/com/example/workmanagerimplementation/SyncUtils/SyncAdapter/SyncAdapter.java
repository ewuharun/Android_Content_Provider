package com.example.workmanagerimplementation.SyncUtils.SyncAdapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataServices;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSync;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSyncModel;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.JsonParser;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.NetworkStream;
import com.example.workmanagerimplementation.data.DataContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by Md.harun or rashid on 25,March,2021
 * BABL, Bangladesh,
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver contentResolver;
    private ArrayList<String> allData;
    private DataServices dataServices;
    private Context mContext;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.contentResolver=context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.contentResolver=context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        allData=downloadAllTableDataFromServer();
        Log.e("alldata",allData.toString());
    }



    private ArrayList<String> downloadAllTableDataFromServer() {
        String service="all";

        String applicationUrl=getContext().getString(R.string.APPLICATION_URL);

        //Parsing the sync_data_down.xml file for collecting the api
        dataServices=new DataServices(getContext());
        ArrayList<DataSync> downServices=dataServices.dataDownServices();


        ArrayList<String> allData=new ArrayList<>();

        for(DataSync dataRule : downServices ){
            if(service.equalsIgnoreCase("all")){
                //dataRule.getServiceUrl collect the partial data from dataDownServices Method
                Log.e("dataRule",new Gson().toJson(dataRule));
                //After Collecting the partial api we download the data form the
                //url by making the url complete and we use here NetworkStream Class for
                //downloading the data

                String resultData = new NetworkStream().getStream(applicationUrl + dataRule.getServiceUrl(), 2, null);
                Log.e("resultDataDown",resultData);

                ArrayList<String> allTableNameList= JsonParser.ifValidJsonGetTable(resultData);

                Log.e("allTable",allTableNameList.toString());

                if(allTableNameList!=null){
                    for(String tableName : allTableNameList){
                        Log.e("tableName",tableName);

                        Uri uri = DataContract.getUri(tableName);
                        Log.e("Uri",uri.toString());



                        HashMap<String,String> tableData=new DataSyncModel(contentResolver).getUniqueColumn(uri,dataRule.getUniqueColumn(),dataRule.getWhereCondition());
                        Log.e("tableData",tableData.toString());
                        HashMap<String, ContentValues> resultContentValuesList = JsonParser.getColIdAndValues(resultData, tableName);
                        Log.e("resultcon",resultContentValuesList.toString());

                        //Inserting the data into the sqlite local database
                        for (String key : resultContentValuesList.keySet()) {
                            if (!tableData.containsKey(key)) {
                                ContentValues value = resultContentValuesList.get(key);

                                Uri insertUri = contentResolver.insert(uri, value);
                                Log.d(tableName, insertUri.getPath());
                            } else {
                                Log.e(tableName + key, ":Already Exist");
                            }
                        }

                    }
                }
                allData.add(resultData+"\n\n");
            }
        }
        //Log.e("allData",new Gson().toJson(allData));
        return allData;
    }


    @Override
    public void onSyncCanceled() {
        super.onSyncCanceled();
        Log.i(TAG, "");
    }

    @Override
    public void onSecurityException(Account account, Bundle extras, String authority, SyncResult syncResult) {
        super.onSecurityException(account, extras, authority, syncResult);
        Log.i(TAG,"Extras: " + extras);
    }
}
