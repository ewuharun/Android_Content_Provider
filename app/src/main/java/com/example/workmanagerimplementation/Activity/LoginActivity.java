package com.example.workmanagerimplementation.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.workmanagerimplementation.BuildConfig;
import com.example.workmanagerimplementation.Models.AssetModel;
import com.example.workmanagerimplementation.Models.Pojo.Asset;
import com.example.workmanagerimplementation.R;
import com.example.workmanagerimplementation.Session.SessionManager;
import com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers.AssetDownloadWorker;
import com.example.workmanagerimplementation.SyncUtils.BackgroundWorkers.DataDownWorker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn, exitBtn;
    private ImageView CompanyLogo, BablLogo;
    private TextView ComapnyName, BablTitile, CompanyTitle;
    private TextInputLayout userNameInputLayout, passwordInputLayout;
    private TextInputEditText usernameEt, passwordEt;

    private RelativeLayout colorlayout;

    //Variable for storing the value of Editext (user Input)
    String username, password;
    Asset asset;

    private SessionManager sessionManager;
    private View view;
    private String[] PERMISSION={Manifest.permission.INTERNET,Manifest.permission.CAMERA};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        if(checkNetworkConnection()){
            asset=new Asset();
            fetchAssetFromServer("Shah Cement Smart Sales",asset);
        }

        loginButtonClicked();
        exitButtonClicked();

    }

    private void showAlertDialogToOnData() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("You must allow permission");
        //builder.setMessage("mesage");

        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int PERMISSION_ALL=1;


                if(!hasPermissions(LoginActivity.this,PERMISSION)){
                    ActivityCompat.requestPermissions(LoginActivity.this,PERMISSION,PERMISSION_ALL);
                }

            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }



    public void fetchAssetFromServer(String project_title, Asset asset) {

        Data data = new Data.Builder()
                        .putString(AssetDownloadWorker.TASK, project_title).build();
        OneTimeWorkRequest assetDownloadRequest = new OneTimeWorkRequest.Builder(AssetDownloadWorker.class)
                        .setInputData(data)
                        .build();
        WorkManager.getInstance().enqueue(assetDownloadRequest);
        WorkManager.getInstance().getWorkInfoByIdLiveData(assetDownloadRequest.getId())
                        .observe(LoginActivity.this, new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {
                                if (workInfo.getState() == WorkInfo.State.RUNNING) {
                                    Toast.makeText(getApplicationContext(), "Running", Toast.LENGTH_SHORT).show();
                                    Log.e("running", "djf");
                                }
                                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                    Toast.makeText(getApplicationContext(), "Succed", Toast.LENGTH_SHORT).show();
                                    Log.e("succ", "djf");
                                }
                                if (workInfo.getState().isFinished()) {
                                    Toast.makeText(getApplicationContext(), "finished", Toast.LENGTH_SHORT).show();
                                    String response = workInfo.getOutputData().getString(AssetDownloadWorker.TASK);
                                    Log.e("finished", response);

                                    loadData(response, asset);

                                }
                            }
                        });

    }
    private void loadData(String response, Asset asset) {
        AssetModel assetModel = new AssetModel();
        asset = assetModel.getAssetData(response);
        setAssetToLoginView(asset);
    }

    private void setAssetToLoginView(Asset asset) {
        ComapnyName.setText(asset.getCompanyName());
        BablTitile.setText(asset.getBabl_title());
        CompanyTitle.setText(asset.getProjectName());

        Glide.with(LoginActivity.this).load(asset.getCompanyLogo()).into(CompanyLogo);
        Glide.with(LoginActivity.this).load(asset.getBabl_logo()).into(BablLogo);
        colorlayout.setBackgroundColor(Color.parseColor(asset.getColor()));

    }


            private boolean gettingInputTextValue() {
                username = usernameEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();

                boolean isValid = validate(username, password);
                return isValid;
            }

            private boolean validate(String username, String password) {
                userNameInputLayout.setError(null);
                passwordInputLayout.setError(null);

                AssetModel assetModel = new AssetModel();

                if (username.isEmpty() && password.isEmpty()) {
                    userNameInputLayout.setError("Username can't be empty ");
                    passwordInputLayout.setError("Password can't be empty ");
                    return false;
                }
                if (username.isEmpty()) {
                    userNameInputLayout.setError("Username can't be empty");
                    return false;
                }
                if (password.isEmpty()) {
                    passwordInputLayout.setError("Password can't be empty");
                    return false;
                }
                return true;
            }

            private void exitButtonClicked() {
                exitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }

            private void loginButtonClicked() {
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isValid = gettingInputTextValue();
                        if (isValid == true) {
                            boolean isNetworkConnected = checkNetworkConnection();
                            if (isNetworkConnected) {
                                sessionManager.saveLoginSession("SR", "12345");

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("You must on your mobile data....");
                                AlertDialog alertDialog= builder.create();
                                alertDialog.show();
                                alertDialog.setCancelable(true);
                            }

                        }
                    }
                });
            }

            private boolean checkNetworkConnection() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
            }

            private void init() {
                CompanyLogo = findViewById(R.id.CompanyLogo);
                ComapnyName = findViewById(R.id.CompanyName);
                CompanyTitle = findViewById(R.id.CompanyTitle);
                usernameEt = findViewById(R.id.userNameEt);
                passwordEt = findViewById(R.id.passwordEt);
                userNameInputLayout = findViewById(R.id.usernameInputLayout);
                passwordInputLayout = findViewById(R.id.passwordInputLayout);
                BablLogo = findViewById(R.id.bablLogo);
                BablTitile = findViewById(R.id.bablTitle);
                exitBtn = findViewById(R.id.exitBtn);
                loginBtn = findViewById(R.id.loginBtn);
                colorlayout = findViewById(R.id.CompanyColor);

                sessionManager = new SessionManager(getApplicationContext());
            }

            @Override
            protected void onStart() {
                super.onStart();
                if(!hasPermissions(LoginActivity.this,PERMISSION)){
                    showAlertDialogToOnData();
                }

                boolean isUserLoggedIn = isUserLoggedIn();
                if (isUserLoggedIn) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            private boolean isUserLoggedIn() {
                return sessionManager.getIsLoggedIn();
            }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}