package com.example.workmanagerimplementation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.ContentResolver;
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
import com.example.workmanagerimplementation.Models.Pojo.Truck;
import com.example.workmanagerimplementation.Models.TruckSizeModel;
import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers.DataDownWorker;
import com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers.DataUpWorker;
import com.example.workmanagerimplementation.data.DBHandler;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button startTaskBtn;
    private TextView workStatusTv;
    private EditText nameEt,ageEt,phoneEt,emailEt;
    private DBHandler dbHandler;
    private SQLiteDatabase db;
    private ContentResolver contentResolver;

    private String name,phone,email;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize the Variables of the activity_main layout
        initVariables();

        ArrayList<Truck> trucks=new ArrayList<>();

        TruckSizeModel truckSize=new TruckSizeModel(getContentResolver());
        trucks=truckSize.getTruckNameList();

        Log.e("tru",new Gson().toJson(trucks));
        Toast.makeText(this, new Gson().toJson(trucks), Toast.LENGTH_SHORT).show();


        //workStatusTv.setText(new Gson().toJson(trucks));

        //creating constraints
//        Constraints constraints = new Constraints.Builder()
//                .setRequiresCharging(true) // you can add as many constraints as you want
//                .build();

        //creating a data object
        //to pass the data with workRequest
        //we can put as many variables needed
        Data data=new Data.Builder()
                .putString(DataUpWorker.TASK_DESC,"The task data passed from main activity")
                .build();


        //this is the subclass of work request
        final OneTimeWorkRequest workRequest=new OneTimeWorkRequest.Builder(DataUpWorker.class)
                .setInputData(data)
                //.setConstraints(constraints)
                .build();

        WorkManager.getInstance().enqueue(workRequest);

        //while clicking on the startTaskBtn
        startTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=nameEt.getText().toString().trim();
                age=Integer.valueOf(ageEt.getText().toString().trim());
                phone=phoneEt.getText().toString().trim();
                email=emailEt.getText().toString().trim();

                Employee employee=new Employee(name,age,phone,email,0);
                EmployeeModel employeeModel=new EmployeeModel(getContentResolver());

                employeeModel.insertEmployee(employee);
                employeeModel.getEmployeeList();

                Log.e("employee",new Gson().toJson(employeeModel.getEmployeeList()));

            }
        });

        //observ the status of the background work done by WorkManager
        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId())
                .observe(MainActivity.this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        //Receiving the Data Back
                        if(workInfo!=null && workInfo.getState().isFinished()){
                            //workStatusTv.setText(workInfo.getOutputData().getString(DataDownWorker.TASK_DESC));
                            //Log.e("none",workInfo.getOutputData().getString(DataUpWorker.TASK_DESC));
                        }
                        //workStatusTv.setText(workInfo.getState().name()+"\n");
                    }
                });
    }


    private void initVariables() {
        startTaskBtn=findViewById(R.id.startWorkBtn);
        workStatusTv=findViewById(R.id.workStatusTv);
        dbHandler=new DBHandler(getApplicationContext());

        nameEt=findViewById(R.id.nameEt);
        ageEt=findViewById(R.id.ageEt);
        phoneEt=findViewById(R.id.phoneEt);
        emailEt=findViewById(R.id.emailEt);
    }


}