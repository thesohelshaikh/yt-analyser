package com.thesohelshaikh.ytanalyser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String API_KEY = "AIzaSyDR6pW44r1itzKNBJg3U0mXOkbZCoXhkhE";
    private final String code = "-QMg39gK624";
    private final YTService service = new YTService(MainActivity.this);

    private EditText edURL;
    private Button btnAnalyse;
    private ClipboardManager clipboardManager;
    TextView videoTitleTextView;
    TextView channelTitleTextView;
    TextView durationTextView;
    ImageView thumbnailImageView;
    ListView durationsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edURL = findViewById(R.id.ed_url);
        btnAnalyse = findViewById(R.id.btn_analyse);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        videoTitleTextView = findViewById(R.id.tv_videoTitle);
        channelTitleTextView = findViewById(R.id.tv_channelTitle);
        durationTextView = findViewById(R.id.tv_duration);
        thumbnailImageView = findViewById(R.id.iv_thumbnail);
        durationsListView = findViewById(R.id.lv_durations);

        getStringFromClipboard();


        btnAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iDfromURL = UtilitiesManger.getIDfromURL(edURL.getText().toString());
                service.getDuration(iDfromURL, new YTService.VolleyResponseListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(final VideoModel videoModel) {

                        service.getVideoDetails(videoModel.getId(), videoModel,
                                new YTService.VideoDetailsListener() {
                            @Override
                            public void onError(String errorMessage) {

                            }

                            @Override
                            public void onResponse(VideoModel model) {
                                Toast.makeText(MainActivity.this, model.getTitle(),
                                        Toast.LENGTH_SHORT).show();
                                HashMap<String, Long> durations =
                                        UtilitiesManger.parseTime(model.getDuration());
                                videoTitleTextView.setText(model.getTitle());
                                channelTitleTextView.setText(model.getChannelTitle());
                                durationTextView.setText(durations.get("1") + "");
                                Picasso.get().load(videoModel.getThumbnailURL()).into(thumbnailImageView);
                                List<Long> dlist = new ArrayList<>(durations.values());
                                List<String> plist = new ArrayList<>(durations.keySet());
                                DurationsAdapter adapter = new DurationsAdapter(MainActivity.this
                                        , plist, dlist);
                                durationsListView.setAdapter(adapter);
                            }
                        });
//                        Date date = UtilitiesManger.parseTime(response);
//
//                        if (date == null) {
//                            Toast.makeText(MainActivity.this, "Could not parse date", Toast
//                            .LENGTH_SHORT).show();
//                            return;
//                        }
//                        Toast.makeText(MainActivity.this, "" + date.getTime(), Toast
//                        .LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public String getStringFromClipboard() {
        //TODO: verify this works
        ClipData primaryClip = clipboardManager.getPrimaryClip();
        if (primaryClip != null) {
            return primaryClip.getItemAt(0).getText().toString();
        }
        return "";
    }


    @Override
    protected void onResume() {
        super.onResume();
        edURL.setText(getStringFromClipboard());
    }
}