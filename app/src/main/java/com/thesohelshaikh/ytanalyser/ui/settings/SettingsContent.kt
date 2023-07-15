package com.thesohelshaikh.ytanalyser.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thesohelshaikh.ytanalyser.BuildConfig


@Composable
fun SettingsContent(showDialog: MutableState<Boolean>) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDialog.value = true }
            ) {
                Text(
                    text = "Clear local cache",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Divider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToGithubProfile(context)
                    }
            ) {
                Text(
                    text = "About the developer",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Divider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /*TODO*/ }
            ) {
                Text(
                    text = "Open source libraries",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Divider()

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
        ) {
            Text(
                text = "App version: ${BuildConfig.VERSION_NAME}",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center

            )
            Text(
                text = "Made with ❤️ in 🇮🇳",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun navigateToGithubProfile(context: Context) {
    val url = "https://github.com/thesohelshaikh"
    val i = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(i)
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    SettingsContent(showDialog = remember {
        mutableStateOf(false)
    })
}