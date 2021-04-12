package com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.NetWorkUtils.NetworkAsset;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md.harun or rashid on 12,April,2021
 * BABL, Bangladesh,
 */
public class AssetDownloadWorker extends Worker {
    private ContentResolver contentResolver;
    public static final String TASK="task_desc";

    public AssetDownloadWorker(Context context,WorkerParameters workerParams) {
        super(context, workerParams);
        this.contentResolver=context.getContentResolver();

    }

    @NonNull
    @Override
    public Result doWork() {
        String project_title=getInputData().getString(TASK);

        String tableName=getApplicationContext().getString(R.string.TBL_LOGIN_INFO_DATA);

        String response=fetchAssetData(tableName,project_title);

        Data data=new Data.Builder()
                .putString(TASK,response)
                .build();



        return Result.success(data);
    }

    private String fetchAssetData(String tableName,String project_title) {
        String url=getApplicationContext().getString(R.string.ASSET_URL);
        List<NameValuePair> param=new ArrayList<>();
        param.add(new BasicNameValuePair("project_title",project_title));
        Log.e("project title",new Gson().toJson(param));
        String response= new NetworkAsset().getStream(url,2,param);
        Log.e("Response",response);


        return response;
    }
}
