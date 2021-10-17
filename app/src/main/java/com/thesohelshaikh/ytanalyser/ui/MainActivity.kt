package com.thesohelshaikh.ytanalyser.ui;

import android.os.Bundle;
import android.text.Html;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.thesohelshaikh.ytanalyser.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle(
                HtmlCompat.fromHtml(
                        "<font color=\"#FFFFFF\">" + getString(R.string.app_name) + "</font>",
                        HtmlCompat.FROM_HTML_MODE_LEGACY));

//        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.app_name) + "</font>", 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}