package com.example.workmanagerimplementation.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.PortUnreachableException;
import java.security.PublicKey;

/**
 * Created by Md.harun or rashid on 21,March,2021
 * BABL, Bangladesh,
 */
public class DataContract {
    public static final String CONTENT_AUTHORITY="com.example.workmanagerimplementation.data";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_TRUCK="tbld_truck_size_new";
    public static final String PATH_EMPLOYEE="tbl_testdata";


    public static Uri getUri(String tableName){
        return BASE_CONTENT_URI.buildUpon().appendPath(tableName).build();
    }

    public static class TruckSizeEntry implements BaseColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRUCK).build();
        public static final String TABLE_NAME="tbld_truck_size_new";
        public static final String COLUMN_ID="column_id";
        public static final String NAME="name";
    }

    public static class EmployeeEntry implements BaseColumns{
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_EMPLOYEE).build();
        public static final String TABLE_NAME="tbl_testdata";
        public static final String NAME="name";
        public static final String AGE="age";
        public static final String PHONE="phone";
        public static final String EMAIL="email";
        public static final String COLUMN_ID="column_id";
        public static final String IS_SYNCED="is_synced";
    }

}
