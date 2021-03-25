package com.example.workmanagerimplementation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
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
import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.SyncUtils.SyncAdapter.SyncUtils;
import com.example.workmanagerimplementation.data.DBHandler;
import com.example.workmanagerimplementation.data.DataContract;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button startTaskBtn, oneTimeWorkReqBtn, logoutBtn;
    private TextView workStatusTv;
    private EditText nameEt, ageEt, phoneEt, emailEt;
    private DBHandler dbHandler;
    private SQLiteDatabase db;
    private ContentResolver contentResolver;
    private EmployeeModel employeeModel;

    private String name, phone, email;
    private int age;


    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = DataContract.CONTENT_AUTHORITY;
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.example.workmanagerimplementation";
    // The account name
    public static final String ACCOUNT = "placeholderaccount";
    // Instance fields
    Account mAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the Variables of the activity_main layout
        initVariables();


        SyncUtils.createSyncAccount(this);

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



        //this is the subclass of work request
//        final PeriodicWorkRequest workRequest=new PeriodicWorkRequest.Builder(DataUpWorker.class,16, TimeUnit.MINUTES).build();

        //this is the subclass of work request



        //WorkManager.getInstance().enqueue(workRequest1);
        //WorkManager.getInstance().enqueue(workRequest);

        //employeeModel.getEmployeeList();


        //WorkManager.getInstance().cancelWorkById(workRequest.getId());

        //Log.e("employee",new Gson().toJson(employeeModel.getEmployeeList()));

        //while clicking on the startTaskBtn



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        oneTimeWorkReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.e("StartTime", String.valueOf(new Date().getTime()));
//
//                WorkManager.getInstance().enqueue(workRequest1);
//
//                ArrayList<Sales> sales = new ArrayList<>();
//                SalesModel salesModel = new SalesModel(getContentResolver());
//                sales = salesModel.salesOrder();
//
//                Log.e("sizeData", String.valueOf(sales.size()));
//
//                Log.e("dataDown", new Gson().toJson(sales));




                SyncUtils.forceRefreshAll(MainActivity.this);
                Toast.makeText(MainActivity.this, "Now check logcat", Toast.LENGTH_SHORT)
                        .show();
            }
        });




    }


    private void initVariables() {
        startTaskBtn = findViewById(R.id.startWorkBtn);
        oneTimeWorkReqBtn = findViewById(R.id.oneTimeWorkBtn);
        workStatusTv = findViewById(R.id.workStatusTv);
        dbHandler = new DBHandler(getApplicationContext());

        nameEt = findViewById(R.id.nameEt);
        ageEt = findViewById(R.id.ageEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);

        logoutBtn = findViewById(R.id.logoutBtn);
    }





    @SuppressLint("MissingPermission")
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

       return null;
    }





}

