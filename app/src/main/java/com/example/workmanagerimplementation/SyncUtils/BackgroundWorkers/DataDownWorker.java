package com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.workmanagerimplementation.NetWorkUtils.NetworkStream;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSync;
import com.example.workmanagerimplementation.data.DBHandler;
import com.example.workmanagerimplementation.data.DataContract;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private DBHandler dbHandler;

    //a public static string that will be used as the key
    //for sending and receiving data
    public static final String TASK_DESC="task_desc";


    public DataDownWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.contentResolver=context.getContentResolver();

    }

    @NonNull
    @Override
    public Result doWork() {

        //getting the input data
        String taskDesc=getInputData().getString(TASK_DESC);

        displayNotification("Worker",taskDesc);

        int startTime= (int) System.currentTimeMillis();

        String TABLE_TEST_DATA=getApplicationContext().getString(R.string.TABLE_TEST_DATA);
        String TABLE_MENU_LIST_DATA=getApplicationContext().getString(R.string.TBL_MENU_LIST_DATA);

        //allData=downloadAllTableDataFromServer();

        //dataDown(TABLE_TEST_DATA);
        getDataForMenuList(TABLE_MENU_LIST_DATA);

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







    private void getDataForMenuList(String tableName) {
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("role_code","SR"));
        String url =getApplicationContext().getString(R.string.GET_MENU_LIST_URL);

        String response=new NetworkStream().getStream(url,2,params);
        Log.e("menu_list_data",response);

        HashMap<String,ContentValues> hashMap=JsonParser.getColIdAndValues(response,tableName);
        Log.e("hasMap",new Gson().toJson(hashMap));

        Uri uri=DataContract.getUri(tableName);
        HashMap<String,String> tableData=new DataSyncModel(contentResolver).getUniqueColumn(uri,"column_id","");


        insertFinal(tableData,hashMap,uri,tableName);

    }









    private void dataDown(String tableName) {
        String url=getApplicationContext().getString(R.string.GET_TEST_DATA_URL);

        String resultData= new NetworkStream().getStream(url,2,null);
        Log.e("response",resultData.toString());

        Uri uri = DataContract.getUri(tableName);

        HashMap<String,ContentValues> values=JsonParser.getColIdAndValuesForTestData(resultData,tableName);

        HashMap<String,String> tableData=new DataSyncModel(contentResolver).getUniqueColumn(uri,"sales_order_id","");

        //String sql = "INSERT INTO TBL_TODAYS_MIS_MERCHANDISING_FILTER_DATA (sales_order_id,so_oracle_id,dealer_name,name,order_date,order_date_time,delivery_date) VALUES (?,?,?,?,?,?,?)";


        insertFinal(tableData,values,uri,tableName);

    }




    public void insert(HashMap<String,String> tableData,HashMap<String,ContentValues> values,Uri uri,String tableName) {
        dbHandler=new DBHandler(getApplicationContext());
        SQLiteDatabase db=dbHandler.getWritableDatabase();
        try {
            db.beginTransaction();
            int i=1;
            for(String key: values.keySet()){
                if(!tableData.containsKey(key)){
                    ContentValues value = values.get(key);
                    long result=db.insert(tableName,null,value);
                    Log.d("MIS",uri.getPath()+key+" index "+i);
                    i++;

                }else{
                    Log.e("MIs" + key, ": Already Exists");
                }
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            db.close();
        }

    }







    public void insertFinal(HashMap<String,String> tableData,HashMap<String,ContentValues> values,Uri uri,String tableName) {
        dbHandler=new DBHandler(getApplicationContext());
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        int sqliteDataSize=tableData.size();
        int backendDataSize=values.size();

        Log.e("sqliteData",String.valueOf(sqliteDataSize));
        Log.e("BackendData",String.valueOf(backendDataSize));


        try {
            db.beginTransaction();

                        db.delete(tableName,null,null);

                        for(String key: values.keySet()){
                            ContentValues value = values.get(key);
                            long result=db.insert(tableName,null,value);
                            Log.d("INSERTED ",uri.getPath()+values.keySet()+" index / ");
                        }






            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            db.close();
        }
    }






    private void is_synced(HashMap<String, String> sqliteData, HashMap<String,ContentValues>responseData,int i) {

        String[] selectionArgs={sqliteData.keySet().toString()};
        String selectionClause=DataContract.SalesEntry.SALES_ORDER_ID+" = ?";
        SQLiteDatabase db=dbHandler.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataContract.MenuListEntry.IS_SYNCED, i);


        int rowId=db.update(DataContract.SalesEntry.TABLE_NAME, contentValues, selectionClause, selectionArgs);
        Log.e("Row Id",String.valueOf(rowId));
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
