package com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataServices;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSync;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.DataSyncModel;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.JsonParser;
import com.example.workmanagerimplementation.SyncUtils.HelperUtils.Maths;
import com.example.workmanagerimplementation.NetWorkUtils.NetworkStream;
import com.example.workmanagerimplementation.data.DataContract;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/**
 * Created by Md.harun or rashid on 22,March,2021
 * BABL, Bangladesh,
 */
public class DataUpWorker extends Worker {

    private DataServices dataServices;
    private ContentResolver contentResolver;

    //This String is to be used as a key for sending or receiving
    //data
    public static final String TASK_DESC="task_desc";

    public DataUpWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.contentResolver=context.getContentResolver();
    }

    @NonNull
    @Override
    public Result doWork() {

        //Getting the Input Data
        String taskDesc=getInputData().getString(TASK_DESC);

        //Passing the Data into main View
        Data data=new Data.Builder()
                .putString(TASK_DESC,"Passing Data")
                .build();


        uploadAllDataIntoTheServer();
        Log.e("Datauploaded",new Gson().toJson(data));



        return Result.success(data);
    }

    private void uploadAllDataIntoTheServer() {
        String service="up";
        String applicationUrl=getApplicationContext().getString(R.string.APPLICATION_URL);

        dataServices=new DataServices(getApplicationContext());
        ArrayList<DataSync> dataUpService=dataServices.dataUpServices();

        for(DataSync dataSync : dataUpService){
            if (service.equalsIgnoreCase("up")){
                Log.e("Service Url : ",dataSync.getServiceUrl()+dataSync.getHttpMethod());

                Uri uri= DataContract.getUri(dataSync.getTableName());
                Log.e("Request Uri : ",uri.toString());

                String url=applicationUrl+dataSync.getServiceUrl();
                Log.e("Url : ",url);

                String condition = dataSync.getUpdateColumn() + "='0'";
                Log.e("condition",condition);

                DataSyncModel dataSyncModel=new DataSyncModel(contentResolver);

                for(ArrayList<NameValuePair> sqliteData : dataSyncModel.getSqliteData(uri,condition)){
                    int httpMethod=1;
                    Log.e("sqliteData ",sqliteData.toString());

                    if(Maths.isInteger(dataSync.getHttpMethod())){
                        httpMethod=Integer.parseInt(dataSync.getHttpMethod());
                    }

                    String dataPut = new NetworkStream().getStream(url, httpMethod, sqliteData);
                    String columnId = JsonParser.ifValidJSONGetColumnId(dataPut);
                    Log.e("RequestResult", dataPut);


                    if (columnId != null) {
                        Log.e("column_id",columnId);
                        dataSyncModel.updateSynced(uri, columnId, dataSync);
                    }

                }

            }
        }
    }
}
