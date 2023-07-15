package com.thesohelshaikh.ytanalyser.ui

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.thesohelshaikh.ytanalyser.ui.home.MyApp


class MainActivity : ComponentActivity() {
    private val receivedIntentData = mutableStateOf("")

    private fun handleSendText(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    // Update UI to reflect text being shared
                    Log.d("TAG", " $it")
                    if (!it.startsWith("http")) return
                    receivedIntentData.value = it
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("TAG", "onNewIntent: $intent")
        handleSendText(intent)
    }

    override fun onResume() {
        super.onResume()
        setContent {
            MyApp(receivedUrl = receivedIntentData.value)
        }
    }
}