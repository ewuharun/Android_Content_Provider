package com.example.workmanagerimplementation.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Request.Priority;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.workmanagerimplementation.Models.Pojo.MainMenu;
import com.example.workmanagerimplementation.R;

import java.util.ArrayList;

/**
 * Created by Md.harun or rashid on 09,April,2021
 * BABL, Bangladesh,
 */
public class GridViewAdapter extends ArrayAdapter<MainMenu> {
    Context mContext;
    ArrayList<MainMenu> mainMenus=new ArrayList<>();
    LayoutInflater layoutInflater;
    public GridViewAdapter(Context context, ArrayList<MainMenu> mainMenus) {
        super(context, 0, mainMenus);
        this.mContext=context;
        this.mainMenus=mainMenus;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView=layoutInflater.from(getContext()).inflate(R.layout.adapter_main_menu_gridview,null);
        MainMenu currentMenu=getItem(position);
        //Log.e("menutitle",currentMenu.getMenuTitle().toString());
        final ImageView imageView=(ImageView) convertView.findViewById(R.id.gridMainMenuImage);
        final TextView textView=(TextView) convertView.findViewById(R.id.gridMainMenuText);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .dontTransform();



        Glide.with(getContext())
                .load("http://120.50.42.151"+currentMenu.getMenuLogoUrl())
                .apply(options)
                .into(imageView);


        textView.setText(currentMenu.getMenuTitle());


        return convertView;
    }
}
