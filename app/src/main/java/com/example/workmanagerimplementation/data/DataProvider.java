package com.example.workmanagerimplementation.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Data;

/**
 * Created by Md.harun or rashid on 21,March,2021
 * BABL, Bangladesh,
 */
public class DataProvider extends ContentProvider {

    DBHandler db;
    static final int TRUCK_SIZE=101;
    static final int INSERT_EMPLOYEE=102;


    private static final UriMatcher sUriMatcher=buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority=DataContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,DataContract.PATH_TRUCK,TRUCK_SIZE);
        matcher.addURI(authority,DataContract.PATH_EMPLOYEE,INSERT_EMPLOYEE);
        return matcher;

    }

    @Override
    public boolean onCreate() {
        db=new DBHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();
        String groupBy=null;
        String limit=null;
        int uriType=sUriMatcher.match(uri);

        switch (uriType){
            case TRUCK_SIZE:
                queryBuilder.setTables(DataContract.TruckSizeEntry.TABLE_NAME);
                break;
            case INSERT_EMPLOYEE:
                queryBuilder.setTables(DataContract.EmployeeEntry.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri : "+uri);

        }

        Cursor cursor=queryBuilder.query(db.getReadableDatabase(),projection,selection,selectionArgs,groupBy,null,sortOrder,limit);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase writableDb=db.getWritableDatabase();
        long id;

        switch (sUriMatcher.match(uri)){
            case TRUCK_SIZE:
                id=writableDb.insert(DataContract.TruckSizeEntry.TABLE_NAME,null,values);
                break;
            case INSERT_EMPLOYEE:
                id=writableDb.insert(DataContract.EmployeeEntry.TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri : "+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return Uri.parse(uri.getPath() + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count=0;
        SQLiteDatabase writableDb=db.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case TRUCK_SIZE:
                count=writableDb.delete(DataContract.TruckSizeEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case INSERT_EMPLOYEE:
                count=writableDb.delete(DataContract.EmployeeEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri : "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count=0;
        SQLiteDatabase writableDb=db.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case TRUCK_SIZE:
                count=writableDb.update(DataContract.TruckSizeEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case INSERT_EMPLOYEE:
                count=writableDb.update(DataContract.EmployeeEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri : "+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
