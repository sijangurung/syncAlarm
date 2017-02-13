package com.gurungsijan.syncalarm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler newHandler = new Handler();

        newHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent newIntent = new Intent(LoadingActivity.this, MainActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(newIntent);
                finish();
            }
        }, 1000);
    }
}
