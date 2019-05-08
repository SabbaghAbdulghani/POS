package com.example.abdulghanis.draftpos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;


public class splashActivity extends AppCompatActivity {

    ProgressBar progbarRun;
    int ind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        progbarRun = findViewById(R.id.progbarRun);
        progbarRun.setProgress(1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runMain();
            }
        }, 1000);

    }


    private void runMain() {
        ind = 1;

        try {
            while (general.Accounts == null || ind < 11) {
                ind++;
                new Thread(new Runnable() {
                    public void run() {
                        progbarRun.setProgress(ind);
                    }
                }).start();

                Thread.currentThread().sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
