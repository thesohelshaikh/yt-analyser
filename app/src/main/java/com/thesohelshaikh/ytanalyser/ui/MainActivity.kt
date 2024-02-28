package com.thesohelshaikh.ytanalyser.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.thesohelshaikh.ytanalyser.data.local.PreferenceDataSource
import com.thesohelshaikh.ytanalyser.data.model.DarkThemeConfig
import com.thesohelshaikh.ytanalyser.data.model.UserData
import com.thesohelshaikh.ytanalyser.ui.home.MyApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val receivedIntentData = mutableStateOf("")

    @Inject
    lateinit var preferenceDataSource: PreferenceDataSource
    private val preferences = mutableStateOf(UserData(DarkThemeConfig.FOLLOWS_SYSTEM))

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferenceDataSource.data.collectLatest {
                    preferences.value = it
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
            MyApp(
                receivedUrl = receivedIntentData.value,
                preferences.value
            )
        }
    }
}