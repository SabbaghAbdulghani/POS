package com.example.abdulghanis.draftpos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.icu.util.TimeUnit;
import android.media.VolumeShaper;
import android.os.Handler;
import android.preference.DialogPreference;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {//implements LoaderCallbacks<Cursor>

    String APILogin = "home/login";
    private static final int REQUEST_READ_CONTACTS = 0;
    Button btLanguage;
    Context context;
    String lang;

    // UI references.
    private EditText mUserName;
    private EditText mPasswordView;
    private Button btLogin;
    private SharedPreferences preferences;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        context=this;
        preferences = getSharedPreferences("POS_PREF", 0);
        loadLanguage();

        mUserName = findViewById(R.id.UseName);
        mPasswordView = findViewById(R.id.password);
        /*mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/

        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        general.ViewProductMenus = preferences.getBoolean("ViewProductMenus", false);
        general.AutoEditSalesItem = preferences.getBoolean("AutoEditSalesItem", false);
        general.PhotoAlbum = preferences.getBoolean("PhotoAlbum", false);
        general.StoreCode = preferences.getString("StoreCode", "ST1");
        general.ServiceURL = preferences.getString("ServiceURL", "http://10.0.2.2:8011/");
        //general.ServiceURL = preferences.getString("ServiceURL", "http://10.0.2.2:8011/");

        mUserName.setText(preferences.getString("userName", ""));

        btLanguage = findViewById(R.id.btLanguage);
        btLanguage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] ListItems = new CharSequence[]{getString(R.string.English), getString(R.string.Arabic)};

                //Configuration config=getBaseContext().getResources().getConfiguration();
                //lang=config.getLocales().get(0).getLanguage();

                int ind=0;
                if(lang.equals("ar"))
                    ind=1;
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                mBuilder.setTitle(getString(R.string.language));
                mBuilder.setSingleChoiceItems(ListItems, ind,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==1){
                                    setLanguage("ar");
                                }else{
                                    setLanguage("en");
                                }
                                recreate();
                                dialog.dismiss();
                            }
                        });
                mBuilder.show();

            }
        });

    }

    private void setLanguage(String newlang){
        try {
            Locale local = new Locale(newlang);
            //Locale.setDefault(local);
            Configuration config=getBaseContext().getResources().getConfiguration();
            config.setLocale(local);
            getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("lang", newlang);
            editor.apply();
        }catch (Exception e){
            e.printStackTrace();;
        }

    }
    private void loadLanguage() {

        lang = preferences.getString("lang", "en");
        setLanguage(lang);
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
        if(userName.equals("")){
            Toast.makeText(this,getString(R.string.missing_value),Toast.LENGTH_LONG).show();
            return;
        }

        general.AppMainActivity=this;
        btLogin.setEnabled(false);
        new general.ApiGetRequest().execute(general.ServiceURL + APILogin
                + "?username=" + userName +"&password=" + password, "authentication");

    }

    public void NotAuthentican(){
        AlertDialog.Builder alert=new AlertDialog.Builder(this).setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.error_invalid_user_name))
                .setIcon(R.drawable.delred1)
                .setCancelable(false).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alert.create().show();
        btLogin.setEnabled(true);
    }
    public void startMainIntent(){
        String userName=mUserName.getText().toString();
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("userName", userName);
        editor.apply();

        //Intent mainIntent=new Intent(getApplicationContext(), MainActivity.class);
        //Intent mainIntent=new Intent(getApplicationContext(), splashActivity.class);
        //startActivity(mainIntent);
        //finish();
        progbarRun = findViewById(R.id.progbarRun);
        progbarRun.setProgress(1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setProgressValue(1);
            }
        }, 1000);

    }

    ProgressBar progbarRun;
    int ind;

    private void setProgressValue(final int progress) {
        if(general.Accounts == null || ind < progbarRun.getMax()) {
            if(progbarRun.getProgress()<progbarRun.getMax())
                progbarRun.setProgress(progress);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ind++;
                    setProgressValue(ind);
                }
            });
            thread.start();
        }else{
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

    }

    public void setServiceUrl(String ErrorMsg){
        AlertDialog.Builder alert=new AlertDialog.Builder(this).setTitle(getString(R.string.app_name))
                .setMessage(ErrorMsg + System.getProperty("line.separator") +getString(R.string.error_url))
                .setIcon(R.drawable.delred1)
                .setCancelable(false).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingActivity=new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(settingActivity);

                    }
                });
        alert.create().show();
        btLogin.setEnabled(true);
    }
}

