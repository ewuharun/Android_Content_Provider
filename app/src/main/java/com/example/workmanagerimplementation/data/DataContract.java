package com.example.workmanagerimplementation.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Md.harun or rashid on 21,March,2021
 * BABL, Bangladesh,
 */
public class DataContract {

    public static final String CONTENT_AUTHORITY="com.example.workmanagerimplementation.data";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);


    public static final String PATH_SALES_ORDER="TBL_TODAYS_MIS_MERCHANDISING_FILTER_DATA";
    public static final String PATH_MENU_LIST="Menu_List_Data";
    public static final String PATH_EMPLOYEE="tbl_test_data";


    public static Uri getUri(String tableName){
        return BASE_CONTENT_URI.buildUpon().appendPath(tableName).build();
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

    public static class SalesEntry implements BaseColumns{
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_SALES_ORDER).build();
        public static final String TABLE_NAME="TBL_TODAYS_MIS_MERCHANDISING_FILTER_DATA";
        public static final String SALES_ORDER_ID="sales_order_id";
        public static final String SO_ORACLE_ID="so_oracle_id";
        public static final String DEALER_NAME="dealer_name";
        public static final String NAME="name";
        public static final String ORDER_DATE="order_date";
        public static final String ORDER_DATE_TIME="order_date_time";
        public static final String DELIVERY_DATE="delivery_date";
        public static final String IS_SYNCED="is_synced";
    }

    public static class MenuListEntry implements BaseColumns{
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENU_LIST).build();
        public static final String TABLE_NAME="Menu_List_Data";
        public static final String COLUMN_ID="column_id";
        public static final String MENU_ID="menu_id";
        public static final String TITLE="title";
        public static final String LOGO="logo";
        public static final String POSITION="position";
        public static final String ROLE_CODE="role_code";
        public static final String IS_SYNCED="is_synced";
    }

}
