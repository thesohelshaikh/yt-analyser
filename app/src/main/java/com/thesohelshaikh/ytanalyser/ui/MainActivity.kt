package com.thesohelshaikh.ytanalyser.ui

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.thesohelshaikh.ytanalyser.ui.home.MyApp
import timber.log.Timber


class MainActivity : ComponentActivity() {
    private val receivedIntentData = mutableStateOf("")

    private fun handleSendText(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    // Update UI to reflect text being shared
                    Timber.d(" $it")
                    if (!it.startsWith("http")) return
                    receivedIntentData.value = it
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("onNewIntent: $intent")
        handleSendText(intent)
    }

    override fun onResume() {
        super.onResume()
        setContent {
            MyApp(receivedUrl = receivedIntentData.value)
        }
    }
}