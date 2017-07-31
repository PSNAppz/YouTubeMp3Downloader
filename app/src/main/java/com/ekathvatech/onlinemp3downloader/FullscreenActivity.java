package com.ekathvatech.onlinemp3downloader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class FullscreenActivity extends AppCompatActivity {
    Intent i ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);
                } catch (Exception e) {

                } finally {

                    i = new Intent(FullscreenActivity.this,
                            MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();

    }
    @Override
    protected void onPause(){
        super.onPause();

    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }


}
