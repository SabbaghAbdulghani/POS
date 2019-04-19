package com.example.abdulghanis.draftpos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {//implements LoaderCallbacks<Cursor>

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    // UI references.
    private EditText mUserName;
    private EditText mPasswordView;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserName = (EditText) findViewById(R.id.UseName);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        preferences = getSharedPreferences("POS_PREF", 0);
        general.ViewProductMenus = preferences.getBoolean("ViewProductMenus", false);
        general.AutoEditSalesItem = preferences.getBoolean("AutoEditSalesItem", false);
        general.PhotoAlbum = preferences.getBoolean("PhotoAlbum", false);
        general.StoreCode = preferences.getString("StoreCode", "ST1");
        general.ServiceURL = preferences.getString("ServiceURL", "http://10.0.2.2:8011/");

        mUserName.setText(preferences.getString("userName", ""));
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    protected void onStope() {
        super.onPause();
        if(general.ActiveUser==null || general.ActiveUser.userId.equals((""))){
            System.exit(0);
        }
    }

    private  void attemptLogin(){
        String userName=mUserName.getText().toString();
        String password=mPasswordView.getText().toString();

        new general.ApiGetRequest().execute(general.ServiceURL + general.getLoginAPI
                + "?username=" + userName +"&password=" + password, "authentication");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(general.ActiveUser !=null && !general.ActiveUser.userId.equals((""))){
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("userName", userName);
            editor.apply();

            //Intent returnIntent = new Intent();
            //returnIntent.putExtra("result",true);
            //setResult(Activity.RESULT_OK,returnIntent);
            Intent loginIntent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
}

