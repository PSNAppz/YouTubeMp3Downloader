package com.ekathvatech.onlinemp3downloader;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;


public class FullscreenActivity extends AppCompatActivity {
    Intent i ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        Typeface titlefont = Typeface.createFromAsset(getAssets(), "fonts/strasua.ttf");
        TextView title = (TextView) findViewById(R.id.textView4);
        Shader shader = title.getPaint().setShader(new LinearGradient(0, 0, 0, title.getLineHeight(), Color.parseColor("#1de9b6"), Color.parseColor("#08AEEA"), Shader.TileMode.REPEAT));
        TextView tv = (TextView) findViewById(R.id.textView4);
        tv.setTypeface(titlefont);
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
