package com.thesohelshaikh.ytanalyser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Date;

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

        getStringFromClipboard();


        btnAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.getDuration(edURL.getText().toString(), new YTService.VolleyResponseListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(final VideoModel videoModel) {

                        service.getVideoDetails(videoModel.getId(), videoModel, new YTService.VideoDetailsListener() {
                            @Override
                            public void onError(String errorMessage) {

                            }

                            @Override
                            public void onResponse(VideoModel model) {
                                Toast.makeText(MainActivity.this, model.getTitle(), Toast.LENGTH_SHORT).show();
                                videoTitleTextView.setText(model.getTitle());
                                channelTitleTextView.setText(model.getChannelTitle());
                                durationTextView.setText(UtilitiesManger.parseTime(model.getDuration()).getTime() + "");
                                Picasso.get().load(videoModel.getThumbnailURL()).into(thumbnailImageView);
                            }
                        });
//                        Date date = UtilitiesManger.parseTime(response);
//
//                        if (date == null) {
//                            Toast.makeText(MainActivity.this, "Could not parse date", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        Toast.makeText(MainActivity.this, "" + date.getTime(), Toast.LENGTH_SHORT).show();
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