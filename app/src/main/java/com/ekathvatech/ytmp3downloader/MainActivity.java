package com.ekathvatech.ytmp3downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity{

    String ytLink = "" ;
    private String fileName;
    private AdView mAdView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface custom_font7 = Typeface.createFromAsset(getAssets(),  "fonts/SourceSansPro-Black.otf");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        editText = (EditText) findViewById(R.id.editText);
        editText.setTypeface(custom_font7);
        final Intent intent = getIntent ();
        final Uri data = intent.getData ();
        if (data != null) {
            final List<String> pathSegments = data.getPathSegments ();
            fileName = pathSegments.get (pathSegments.size () - 1);
        }
        Button button = (Button) findViewById(R.id.button);
        button.setBackgroundColor(getResources().getColor(R.color.accent));
        final TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setTypeface(custom_font7);
        final TextView textView1 = (TextView) findViewById(R.id.textView);
        textView1.setTypeface(custom_font7);
        if(PermissionCheck.readAndWriteExternalStorage(this)){
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ytLink = editText.getText().toString();
                    if(ytLink.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Please enter a valid video URL",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Parsing URL",Toast.LENGTH_SHORT).show();
                    }
                    new YouTubeExtractor(getApplicationContext()) {
                        @Override
                        public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                            if (ytFiles != null) {
                                textView.setText("Title : "+vMeta.getTitle());
                                textView1.setText("Views : "+vMeta.getViewCount());
                                int tag = 140;
                                String downloadUrl = ytFiles.get(tag).getUrl();
                                String url = downloadUrl;
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                request.setTitle(vMeta.getTitle());
                                // in order for this if to run, you must use the android 3.2 to compile your app
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                }
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,vMeta.getTitle()+".mp3");
                                // get download service and enqueue file
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
