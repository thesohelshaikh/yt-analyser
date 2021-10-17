package com.thesohelshaikh.ytanalyser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thesohelshaikh.ytanalyser.R
import androidx.core.text.HtmlCompat
import com.thesohelshaikh.ytanalyser.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color=\"#FFFFFF\">" + getString(R.string.app_name) + "</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

//        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.app_name) + "</font>", 0));
    }

}