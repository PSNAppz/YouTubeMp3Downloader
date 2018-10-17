package com.ekathvatech.onlinemp3downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.codekidlabs.storagechooser.StorageChooser;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity{
    private String ytLink = "";
    private String storage = "";
    private EditText editText;
    private String value1 = "";
    private ImageView imageView;
    private StorageChooser chooser;
    private Button button ;
    private ImageButton shareButton ;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.download);
        shareButton = (ImageButton) findViewById(R.id.share);
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
                .allowAddFolder(true)
                .actionSave(true)
                .build();
        ImageButton changeDir =(ImageButton) findViewById(R.id.button2);
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


        final TextView ek = (TextView) findViewById(R.id.tvekathva);
        ek.setTypeface(custom_font7);
        final TextView title = (TextView) findViewById(R.id.textView3);
        title.setTypeface(titlefont);
        editText = (EditText) findViewById(R.id.editText);
        editText.setTypeface(custom_font7);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=com.ekathvatech.onlinemp3downloaderpro";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download Mp3 from Y0uTub3 Videos using Mega Mp3 Downloader");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        Bundle extras = getIntent().getExtras();
        textView = (TextView) findViewById(R.id.textView5);
        textView.setTypeface(custom_font7);
            if (extras != null) {
                value1 = extras.getString(Intent.EXTRA_TEXT);
                editText.setText(value1);
            }
    }

    protected void onDestroy() {

        super.onDestroy();
    }
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void downloadStart(View view) {
            Toast.makeText(getApplicationContext(), "Please enter a valid video URL", Toast.LENGTH_SHORT).show();


            ytLink = editText.getText().toString();
            if (ytLink.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a valid video URL", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Converting please wait...", Toast.LENGTH_LONG).show();
            }
            new YouTubeExtractor(getApplicationContext()) {
                @Override
                public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                    boolean err = false;
                    if (ytFiles != null) {
                        textView.setText("Title : " + vMeta.getTitle());
                        Glide.with(getApplicationContext()).load(vMeta.getMqImageUrl()).into(imageView);
                        int tag = 140;
                        String downloadUrl = "";
                        try {
                            downloadUrl = ytFiles.get(tag).getUrl();
                        }catch (Exception e){
                            tag = 171;
                            downloadUrl = "";
                            try {
                                downloadUrl = ytFiles.get(tag).getUrl();
                            }catch (Exception c) {
                                err = true;
                            }
                        }
                        if(err){
                            Toast.makeText(getApplicationContext(), "Sorry ! This link is not supported. Sorry for the inconvenience caused.", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Download has started !", Toast.LENGTH_LONG).show();
                        }
                        DownloadManager.Request request;
                        String extension;
                        if (!downloadUrl.toString().isEmpty()) {
                            request = new DownloadManager.Request(Uri.parse(downloadUrl));
                            extension = ".mp3";
                            request.setTitle(vMeta.getTitle());
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(storage, vMeta.getTitle() + extension);
                            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                        }
                    }
                }
            }.extract(ytLink, true, true);
    }
}

