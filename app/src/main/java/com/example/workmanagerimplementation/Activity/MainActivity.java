package com.example.workmanagerimplementation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workmanagerimplementation.Adapter.GridViewAdapter;
import com.example.workmanagerimplementation.Models.EmployeeModel;
import com.example.workmanagerimplementation.Models.MainMenuModel;
import com.example.workmanagerimplementation.Models.Pojo.Employee;
import com.example.workmanagerimplementation.Models.Pojo.MainMenu;
import com.example.workmanagerimplementation.Models.Pojo.Sales;
import com.example.workmanagerimplementation.Models.SalesModel;
import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers.DataDownWorker;
import com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers.DataUpWorker;
import com.example.workmanagerimplementation.data.DBHandler;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button logoutBtn;
    private TextView workStatusTv;
    private DBHandler dbHandler;
    private SQLiteDatabase db;
    private ContentResolver contentResolver;
    private EmployeeModel employeeModel;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating constraints
//        Constraints constraints = new Constraints.Builder()
//                .setRequiresCharging(true) // you can add as many constraints as you want
//                .build();


        //this is the subclass of periodic work request
//        final PeriodicWorkRequest workRequest=new PeriodicWorkRequest.Builder(DataUpWorker.class,16, TimeUnit.MINUTES).build();


        initVariables();
        SyncData();
        MainMenuModel mainMenuModel=new MainMenuModel(getContentResolver());

        ArrayList<MainMenu> mainMenuArrayList=new ArrayList<MainMenu>();

        mainMenuArrayList=mainMenuModel.getAllMenuList("SR");

        mainMenuModel.sort(mainMenuArrayList);


        Log.e("ArrayList",new Gson().toJson(mainMenuArrayList));

        gridViewAdapter=new GridViewAdapter(getApplicationContext(),mainMenuArrayList);
        gridView.setAdapter(gridViewAdapter);

        menuAction(gridView);












        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void menuAction(GridView gridView) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MainMenu mainMenu= (MainMenu) gridView.getItemAtPosition(position);


                //Toast.makeText(getApplicationContext(),mainMenu.getMenuTitle(), Toast.LENGTH_SHORT).show();
                
                String title=mainMenu.getMenuTitle();
                
                switch (title){
                    case "profile_icon":
                        Toast.makeText(MainActivity.this, "Profile Icon", Toast.LENGTH_SHORT).show();
                        break;
                    case  "verify_retailer":
                        Toast.makeText(MainActivity.this, "Verify_Retailer", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Default", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }

    private void SyncData() {
        Data dataDown=new Data.Builder()
                .putString(DataDownWorker.TASK_DESC,String.valueOf(new Date().getTime()))
                .build();
        final OneTimeWorkRequest dataDownWorkRequest=new OneTimeWorkRequest.Builder(DataDownWorker.class)
                .setInputData(dataDown)
                //.setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(dataDownWorkRequest);
        //observ the status of the background work done by WorkManager
        WorkManager.getInstance().getWorkInfoByIdLiveData(dataDownWorkRequest.getId())
                .observe(MainActivity.this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                        if(workInfo.getState()==WorkInfo.State.RUNNING){
                            Toast.makeText(MainActivity.this, "Running", Toast.LENGTH_SHORT).show();
                        }
                        if (workInfo.getState()==WorkInfo.State.SUCCEEDED){
                            Log.e("EndTime",String.valueOf(new Date().getTime()));
                        }

                        workStatusTv.setText(workInfo.getState().name()+"\n");

                        //Receiving the Data Back
                        if(workInfo!=null && workInfo.getState().isFinished()){

                            //workStatusTv.setText(workInfo.getOutputData().getString(DataDownWorker.TASK_DESC));
                            //Log.e("none",workInfo.getOutputData().getString(DataUpWorker.TASK_DESC));
                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "MainActi", Toast.LENGTH_SHORT).show();
    }

    private void initVariables() {
        workStatusTv=(TextView) findViewById(R.id.workStatusTv);
        dbHandler=new DBHandler(getApplicationContext());
        gridView=findViewById(R.id.gridView);
        logoutBtn=(Button) findViewById(R.id.logoutBtn);
    }


}