package com.thesohelshaikh.ytanalyser.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.thesohelshaikh.ytanalyser.BuildConfig
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.data.model.DarkThemeConfig
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme


@Composable
fun SettingsContent(
    showDialog: MutableState<Boolean>,
    currentTheme: DarkThemeConfig,
    shouldUseClipboard: Boolean,
    onThemeUpdate: (theme: DarkThemeConfig) -> Unit,
    onUseClipboardToggle: () -> Unit,
) {
    val context = LocalContext.current
    val showDialogTheme = remember { mutableStateOf(false) }

    if (showDialogTheme.value) {
        ChooseThemeDialog(showDialogTheme, currentTheme) {
            onThemeUpdate(it)
        }
    }

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
                text = stringResource(R.string.label_appearance),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary

            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDialogTheme.value = true }
            ) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigateToLanguageSettings(context) }
            ) {
                Text(
                    text = stringResource(id = R.string.label_language),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.label_settings_use_clipboard),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                    )
                    Text(
                        text = stringResource(R.string.label_settings_clipboard_helper),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 16.dp)
                ) {
                    Switch(
                        checked = shouldUseClipboard,
                        onCheckedChange = { onUseClipboardToggle() },
                    )
                }
            }

            HorizontalDivider()
            Text(
                text = stringResource(R.string.label_data_usage),
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
                    text = stringResource(R.string.label_clear_local_cache),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            HorizontalDivider()
            Text(
                text = stringResource(R.string.label_legal),
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
                    text = stringResource(R.string.label_open_source_libraries),
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
                        navigateToPrivacyPrivacyPolicy(context)
                    }
            ) {
                Text(
                    text = stringResource(R.string.label_privacy_policy),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            HorizontalDivider()
            Text(
                text = stringResource(R.string.label_about),
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
                    text = stringResource(R.string.label_developer),
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
                    text = stringResource(R.string.label_source_code),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            HorizontalDivider()

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

private fun navigateToLanguageSettings(context: Context) {
    val intent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val i = Intent(Settings.ACTION_APP_LOCALE_SETTINGS)
        i.data = Uri.fromParts("package", context.packageName, null)
        i
    } else {
        val i = Intent(Intent.ACTION_MAIN)
        i.setClassName("com.android.settings", "com.android.settings.LanguageSettings")
        i
    }
    context.startActivity(intent)
}

private fun navigateToOpenSourceLicences(context: Context) {
    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
}

fun navigateToPrivacyPrivacyPolicy(context: Context) {
    val url = "https://www.freeprivacypolicy.com/live/09df12f9-497e-44cb-b83d-248b479b2890"
    navigateToWebpage(url, context)
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

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    AppTheme {
        SettingsContent(
            showDialog = remember {
                mutableStateOf(false)
            },
            currentTheme = DarkThemeConfig.LIGHT,
            shouldUseClipboard = false,
            onThemeUpdate = {
                /* no-op */
            },
            onUseClipboardToggle = {
                /* no-op */
            }
        )
    }
}