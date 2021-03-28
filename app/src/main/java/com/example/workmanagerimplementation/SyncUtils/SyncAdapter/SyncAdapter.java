package com.example.workmanagerimplementation.SyncUtils.SyncAdapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataServices;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSync;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSyncModel;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.JsonParser;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.NetworkStream;
import com.example.workmanagerimplementation.data.DBHandler;
import com.example.workmanagerimplementation.data.DataContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
    private DBHandler dbHandler=new DBHandler(getContext());


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
        int startTime= (int) System.currentTimeMillis();

        //allData=downloadAllTableDataFromServer();
        dataDown();

        int totalTimeRequired= (int) (System.currentTimeMillis()-startTime);

        //hh:mm:ss
        String time=String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(totalTimeRequired),
                TimeUnit.MILLISECONDS.toMinutes(totalTimeRequired) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalTimeRequired)),
                TimeUnit.MILLISECONDS.toSeconds(totalTimeRequired) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTimeRequired)));

        Log.e("TotalTimeRequired",time);
    }

    private void dataDown() {
        String url="http://120.50.42.151/mobile_api/api/Merchandising/harunvaitest";

        String resultData= new NetworkStream().getStream(url,2,null);
        Log.e("result",resultData.toString());

        Uri uri = DataContract.getUri("TBL_TODAYS_MIS_MERCHANDISING_FILTER_DATA");

        HashMap<String,ContentValues> values=JsonParser.getColIdAndValues(resultData,"TBL_TODAYS_MIS_MERCHANDISING_FILTER_DATA");

        HashMap<String,String> tableData=new DataSyncModel(contentResolver).getUniqueColumn(uri,"sales_order_id","");

        //String sql = "INSERT INTO TBL_TODAYS_MIS_MERCHANDISING_FILTER_DATA (sales_order_id,so_oracle_id,dealer_name,name,order_date,order_date_time,delivery_date) VALUES (?,?,?,?,?,?,?)";


        insert(tableData,values,uri);


//        for (String key : values.keySet()) {
//            if (!tableData.containsKey(key)) {
//                ContentValues value = values.get(key);
//
//                boolean isInsert=insert(value);
//                //Uri insertUri = contentResolver.insert(uri, value);
//
//                Log.d("MIS", uri.getPath()+key);
//            } else {
//                Log.e("MIS" + key, ":Already Exist");
//            }
//        }


    }
    public void insert(HashMap<String,String> tableData,HashMap<String,ContentValues> values,Uri uri) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        boolean wasSuccess = true;
        try {
            db.beginTransaction();
            int i=1;
            for(String key: values.keySet()){
                if(!tableData.containsKey(key)){
                    ContentValues value = values.get(key);
                    long result=db.insert("TBL_TODAYS_MIS_MERCHANDISING_FILTER_DATA",null,value);
                    Log.d("MIS",uri.getPath()+key+" index =  : "+i);
                    i++;

                }else{
                    Log.e("MIs" + key, ": Already Exists : "+ i);
                    i++;
                }
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            db.close();
        }

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
                        int i=1;
                        //Inserting the data into the sqlite local database
                        for (String key : resultContentValuesList.keySet()) {
                            if (!tableData.containsKey(key)) {
                                ContentValues value = resultContentValuesList.get(key);

                                Uri insertUri = contentResolver.insert(uri, value);


                                Log.d(tableName, insertUri.getPath()+" / "+i);
                                i++;
                            } else {
                                Log.e(tableName + key + "/"+i, ":Already Exist");
                                i++;

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
