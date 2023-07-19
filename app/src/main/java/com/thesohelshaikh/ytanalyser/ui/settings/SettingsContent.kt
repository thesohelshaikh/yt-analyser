package com.thesohelshaikh.ytanalyser.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
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
        Text(
            text = "Legal",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary

        )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToOpenSourceLicences(context)
                    }
            ) {
                Text(
                    text = "Open source libraries",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        /*TODO*/
                    }
            ) {
                Text(
                    text = "Privacy Policy",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Divider()
            Text(
                text = "Data usage",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary

            )
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
            Text(
                text = "Appearance",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary

            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /*TODO*/ }
            ) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Divider()
            Text(
                text = "About",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary

            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToGithubProfile(context)
                    }
            ) {
                Text(
                    text = "Developer",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToGithubRepo(context)
                    }
            ) {
                Text(
                    text = "Source code",
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

private fun navigateToOpenSourceLicences(context: Context) {
    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
}

fun navigateToGithubProfile(context: Context) {
    val url = "https://github.com/thesohelshaikh"
    navigateToWebpage(url, context)
}

fun navigateToGithubRepo(context: Context) {
    val url = "https://github.com/thesohelshaikh/yt-analyser"
    navigateToWebpage(url, context)
}


fun navigateToWebpage(url: String, context: Context) {
    val i = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(i)
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        SettingsContent(showDialog = remember {
            mutableStateOf(false)
        })
    }
}