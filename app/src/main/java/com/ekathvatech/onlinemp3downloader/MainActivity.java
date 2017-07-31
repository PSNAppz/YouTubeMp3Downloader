package com.ekathvatech.onlinemp3downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity {

    String ytLink = "";
    String storage = "";
    private AdView mAdView;
    private EditText editText;
    String value1 = "";
    private ImageView imageView;
    private StorageChooser chooser;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (TextView) findViewById(R.id.textView3);
        Shader shader = title.getPaint().setShader(new LinearGradient(0, 0, 0, title.getLineHeight(), Color.parseColor("#1de9b6"), Color.parseColor("#08AEEA"), Shader.TileMode.REPEAT));
        storage = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("STORAGE_PATH", "");
        if(storage.equals("")){
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("STORAGE_PATH",Environment.DIRECTORY_DOWNLOADS).apply();
            storage = Environment.DIRECTORY_DOWNLOADS;

        }
        chooser = new StorageChooser.Builder()
                .allowCustomPath(true)
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .withActivity(MainActivity.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .build();
        Button changeDir = (Button) findViewById(R.id.button2);
        changeDir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Current directory :"+storage, Toast.LENGTH_SHORT).show();
                chooser.show();
            }
        });
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("STORAGE_PATH",path).apply();
                storage = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("STORAGE_PATH", "");
            }
        });
        Typeface custom_font7 = Typeface.createFromAsset(getAssets(), "fonts/PT_Sans-Web-Regular.otf");
        Typeface titlefont = Typeface.createFromAsset(getAssets(), "fonts/strasua.ttf");
        imageView = (ImageView) findViewById(R.id.imageView2);

        mAdView = (AdView) findViewById(R.id.adView);
        final TextView ek = (TextView) findViewById(R.id.tvekathva);
        ek.setTypeface(custom_font7);
        final TextView title = (TextView) findViewById(R.id.textView3);
        title.setTypeface(titlefont);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        editText = (EditText) findViewById(R.id.editText);
        editText.setTypeface(custom_font7);
        Bundle extras = getIntent().getExtras();
        Button button = (Button) findViewById(R.id.button);
        final TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setTypeface(custom_font7);
        final TextView textView1 = (TextView) findViewById(R.id.textView);
        textView1.setTypeface(custom_font7);
        if (PermissionCheck.readAndWriteExternalStorage(this)) {
            if (extras != null) {
                value1 = extras.getString(Intent.EXTRA_TEXT);
                editText.setText(value1);
            }
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ytLink = editText.getText().toString();
                    if (ytLink.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid video URL", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Converting please wait...", Toast.LENGTH_SHORT).show();
                    }
                    new YouTubeExtractor(getApplicationContext()) {
                        @Override
                        public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                            if (ytFiles != null) {
                                textView.setText("Title : " + vMeta.getTitle());
                                textView1.setText(" By : " + vMeta.getAuthor());
                                Glide.with(getApplicationContext()).load(vMeta.getMqImageUrl()).into(imageView);
                                int tag = 140;
                                String downloadUrl = ytFiles.get(tag).getUrl();
                                String url = downloadUrl;
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                request.setTitle(vMeta.getTitle());
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(storage, vMeta.getTitle() + ".mp3");
                                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                manager.enqueue(request);
                            }
                        }
                    }.extract(ytLink, true, true);
                }
            });
        }
    }
}

