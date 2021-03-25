package com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataServices;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSyncModel;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.JsonParser;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.NetworkStream;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSync;
import com.example.workmanagerimplementation.data.DataContract;
import com.google.android.material.badge.BadgeDrawable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Md.harun or rashid on 21,March,2021
 * BABL, Bangladesh,
 */
public class DataDownWorker extends Worker {

    //Instance Variable Declaration
    private Context mContext;
    private ContentResolver contentResolver;
    private DataServices dataServices;
    private ArrayList<String> allData;

    //a public static string that will be used as the key
    //for sending and receiving data
    public static final String TASK_DESC="task_desc";

    //Constructor for DataDownWorker Class
    public DataDownWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.contentResolver=context.getContentResolver();
    }

    /*This doWork Method is responsible for doing the work in the
    background and here we download the data from the server*/
    @NonNull
    @Override
    public Result doWork() {

        //getting the input data
        String taskDesc=getInputData().getString(TASK_DESC);

        //Notification show while apps run in the background
        displayNotification("Worker",taskDesc);

        //ArrayList of all String which is coming back from DataDown Service from
        //all api



        int startTime= (int) System.currentTimeMillis();

        allData=downloadAllTableDataFromServer();

        int totalTimeRequired= (int) (System.currentTimeMillis()-startTime);

        //hh:mm:ss
        String time=String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(totalTimeRequired),
                TimeUnit.MILLISECONDS.toMinutes(totalTimeRequired) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalTimeRequired)),
                TimeUnit.MILLISECONDS.toSeconds(totalTimeRequired) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTimeRequired)));

        Log.e("TotalTimeRequired",time);

        //allData will be passed by this Data class in the main view
        Data data=new Data.Builder()
                .putString(TASK_DESC,String.valueOf(new Date().getTime()))
                .build();

        //Return Back the success status with data
        return Result.success();
    }


    /*Displaying a simple notification while task is done*/
    private void displayNotification(String title,String task){
        NotificationManager notificationManager=(NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //Checking out the version of sdk for displaying notification
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("WorkManager","manager",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),"WorkManager")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(1,builder.build());

    }

    //This method is responsible for returning back the context
    public Context getContext() {
        return mContext;
    }


    //after parsing the sync_data_down.xml file we download the data from
    //that api using this method first we collect the partial api
    //then we write the code for completing the api and then we get the
    //data from the server
    public ArrayList<String> downloadAllTableDataFromServer(){
        String service="all";

        String applicationUrl=getApplicationContext().getString(R.string.APPLICATION_URL);

        //Parsing the sync_data_down.xml file for collecting the api
        dataServices=new DataServices(getApplicationContext());
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


}
