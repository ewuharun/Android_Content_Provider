package com.example.workmanagerimplementation.Activity;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workmanagerimplementation.Models.EmployeeModel;
import com.example.workmanagerimplementation.Models.Pojo.Employee;
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
    private Button startTaskBtn,oneTimeWorkReqBtn,logoutBtn;
    private TextView workStatusTv;
    private EditText nameEt,ageEt,phoneEt,emailEt;
    private DBHandler dbHandler;
    private SQLiteDatabase db;
    private ContentResolver contentResolver;
    private EmployeeModel employeeModel;

    private String name,phone,email;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();

        //employeeModel=new EmployeeModel(getContentResolver());







        //workStatusTv.setText(new Gson().toJson(trucks));

        //creating constraints
//        Constraints constraints = new Constraints.Builder()
//                .setRequiresCharging(true) // you can add as many constraints as you want
//                .build();

        //creating a data object
        //to pass the data with workRequest
        //we can put as many variables needed
//        Data data=new Data.Builder()
//                .putString(DataUpWorker.TASK_DESC,"The task data passed from main activity")
//                .build();
        Data dataDown=new Data.Builder()
                .putString(DataDownWorker.TASK_DESC,String.valueOf(new Date().getTime()))
                .build();




        //this is the subclass of work request
//        final PeriodicWorkRequest workRequest=new PeriodicWorkRequest.Builder(DataUpWorker.class,16, TimeUnit.MINUTES).build();

        //this is the subclass of work request
        final OneTimeWorkRequest dataDownWorkRequest=new OneTimeWorkRequest.Builder(DataDownWorker.class)
                .setInputData(dataDown)
                //.setConstraints(constraints)
                .build();


        //WorkManager.getInstance().enqueue(workRequest1);
        //WorkManager.getInstance().enqueue(workRequest);

        //employeeModel.getEmployeeList();



        //WorkManager.getInstance().cancelWorkById(workRequest.getId());

        //Log.e("employee",new Gson().toJson(employeeModel.getEmployeeList()));

        //while clicking on the startTaskBtn
        startTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=nameEt.getText().toString().trim();
                age=Integer.valueOf(ageEt.getText().toString().trim());
                phone=phoneEt.getText().toString().trim();
                email=emailEt.getText().toString().trim();

                //Employee employee=new Employee(name,age,phone,email,0,0);


                //String isInserted=employeeModel.insertEmployee(employee);
                //Toast.makeText(MainActivity.this, isInserted, Toast.LENGTH_SHORT).show();


            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        oneTimeWorkReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("StartTime",String.valueOf(new Date().getTime()));

                WorkManager.getInstance().enqueue(dataDownWorkRequest);

                ArrayList<Sales> sales=new ArrayList<>();
                SalesModel salesModel=new SalesModel(getContentResolver());
                sales=salesModel.salesOrder();

                Log.e("sizeData", String.valueOf(sales.size()));

                Log.e("dataDown",new Gson().toJson(sales));
            }
        });




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


    private void initVariables() {
        startTaskBtn=findViewById(R.id.startWorkBtn);
        oneTimeWorkReqBtn=findViewById(R.id.oneTimeWorkBtn);
        workStatusTv=findViewById(R.id.workStatusTv);
        dbHandler=new DBHandler(getApplicationContext());

        nameEt=findViewById(R.id.nameEt);
        ageEt=findViewById(R.id.ageEt);
        phoneEt=findViewById(R.id.phoneEt);
        emailEt=findViewById(R.id.emailEt);

        logoutBtn=findViewById(R.id.logoutBtn);
    }


}