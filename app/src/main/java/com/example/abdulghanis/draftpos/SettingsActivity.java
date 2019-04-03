package com.example.abdulghanis.draftpos;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    Switch swViewProductMenus;
    Switch swAutoEditSalesItem;
    Switch swPhotoAlbum;
    EditText etStore;
    EditText etURL;
    //EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swViewProductMenus=(Switch)findViewById(R.id.swViewProductMenus);
        swAutoEditSalesItem=(Switch)findViewById(R.id.swAutoEditSalesItem);
        swPhotoAlbum=(Switch)findViewById(R.id.swPhotoAlbum);
        etStore=(EditText)findViewById(R.id.etStore);
        etURL=(EditText)findViewById(R.id.etURL);
        //etUserName=(EditText)findViewById(R.id.etUserName);

        preferences=getSharedPreferences("POS_PREF",0);

        //general.ViewProductMenus=preferences.getBoolean("ViewProductMenus", false);
        //general.PhotoAlbum=preferences.getBoolean("PhotoAlbum", false);
       //general.StoreCode=preferences.getString("StoreCode", "");
        //general.ServiceURL=preferences.getString("ServiceURL", "");


        swViewProductMenus.setChecked(general.ViewProductMenus);
        swPhotoAlbum.setChecked(general.PhotoAlbum);
        etStore.setText(general.StoreCode);
        etURL.setText(general.ServiceURL);
        //etUserName.setText(general.ActiveUser.userName);

        swViewProductMenus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                general.ViewProductMenus=!general.ViewProductMenus;
                swViewProductMenus.setChecked(general.ViewProductMenus);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("ViewProductMenus", general.ViewProductMenus);
                editor.apply();
            }
        });
        swAutoEditSalesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                general.AutoEditSalesItem=!general.AutoEditSalesItem;
                swAutoEditSalesItem.setChecked(general.AutoEditSalesItem);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("AutoEditSalesItem", general.AutoEditSalesItem);
                editor.apply();
            }
        });
        swPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                general.PhotoAlbum=!general.PhotoAlbum;
                swPhotoAlbum.setChecked(general.PhotoAlbum);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("PhotoAlbum", general.PhotoAlbum);
                editor.apply();
            }
        });
        etStore.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                general.StoreCode=etStore.getText().toString();
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("StoreCode", general.StoreCode);
                editor.apply();
            }
        });
        etURL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                general.ServiceURL=etURL.getText().toString();
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("ServiceURL", general.ServiceURL);
                editor.apply();
            }
        });
    }
}
