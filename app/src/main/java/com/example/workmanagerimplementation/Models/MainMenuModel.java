package com.example.workmanagerimplementation.Models;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.example.workmanagerimplementation.Models.Pojo.MainMenu;
import com.example.workmanagerimplementation.data.DBHandler;
import com.example.workmanagerimplementation.data.DataContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Md.harun or rashid on 09,April,2021
 * BABL, Bangladesh,
 */
public class MainMenuModel{
    ContentResolver contentResolver;
    DBHandler db;

    public MainMenuModel(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public ArrayList<MainMenu> getAllMenuList(String role_code){
        ArrayList<MainMenu> mainMenuArrayList=new ArrayList<MainMenu>();

        String[] projection={
                DataContract.MenuListEntry.LOGO,
                DataContract.MenuListEntry.POSITION,
                DataContract.MenuListEntry.TITLE,
                DataContract.MenuListEntry.MENU_ID,
        };

        String[] selctionArgs={""};
        String selectionClause=null;


        selctionArgs[0]=role_code;
        selectionClause = DataContract.MenuListEntry.ROLE_CODE + " = ?";


        Cursor cursor=contentResolver.query(DataContract.MenuListEntry.CONTENT_URI,projection,selectionClause,selctionArgs,null);
        Log.e("Cursor",new Gson().toJson(cursor));
        if(cursor.moveToFirst()){
            do {
                Log.e("FirstCursor",cursor.getString(0));
                Log.e("SecondCursor",cursor.getString(1));
                Log.e("ThirdCursor",cursor.getString(2));
                Log.e("FourthCursor",cursor.getString(3));
                mainMenuArrayList.add(new MainMenu(cursor.getString(2),cursor.getString(0),cursor.getInt(1),cursor.getInt(3)));
            }while (cursor.moveToNext());
        }


        return mainMenuArrayList;
    }

    public ArrayList<MainMenu> sort(ArrayList<MainMenu> mainMenuArrayList){

        Comparator<MainMenu> compareByPosition = new Comparator<MainMenu>() {

            @Override
            public int compare(MainMenu o1, MainMenu o2) {

                return o1.getMenuPosition().compareTo(o2.getMenuPosition());
            }
        };
        Collections.sort(mainMenuArrayList,compareByPosition);

        return mainMenuArrayList;
    }



    public ArrayList<MainMenu> readAllItems() {
        ArrayList<MainMenu> items = new ArrayList<>();

        Cursor cursor = contentResolver.query(DataContract.MenuListEntry.CONTENT_URI,null,null,null,null);



        if (cursor!=null) {
            while (cursor.moveToNext()) {
                // move the cursor to next row if there is any to read it's data
                MainMenu item = readItem(cursor);
                items.add(item);
            }
        }
        return items;
    }

    private MainMenu readItem(Cursor cursor) {
        MainMenu item = new MainMenu();
        item.setMenuTitle(cursor.getString(cursor.getColumnIndex(DataContract.MenuListEntry.IS_SYNCED)));
        return item;

    }



}
