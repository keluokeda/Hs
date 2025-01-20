package com.ke.hs.ui.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.ke.hs.FileService
import com.ke.hs.R
import com.ke.hs.parser.FileTextProviderImpl
import com.ke.hs.parser.HsLogFile
import com.ke.hs.setWindowWidth
import com.ke.hs.ui.theme.HsTheme
import com.tencent.bugly.crashreport.CrashReport

import kotlinx.coroutines.runBlocking


@Composable
fun SettingsRoute(onBack: () -> Unit = {}, toSync: () -> Unit = {}, toLogs: () -> Unit = {}) {
    SettingsScreen(onBack, toSync, toLogs)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    onBack: () -> Unit = {},
    toSync: () -> Unit = {},
    toLogs: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = { Text("设置") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                ListItem(headlineContent = { Text(text = "同步卡牌数据") }, supportingContent = {
                    Text(text = "炉石传说版本更新时，需要同步更新本地数据")
                }, modifier = Modifier.clickable {
                    toSync()
                })
            }

            item {
                ListItem(headlineContent = { Text(text = "联系作者") }, supportingContent = {
                    Text(text = "QQ 913918146")
                })
            }

            item {
                ListItem(
                    headlineContent = { Text(text = "日志管理") },
                    modifier = Modifier.clickable {
                        toLogs()
                    })
            }

            item {
                val url = "https://github.com/keluokeda/Hs"
                ListItem(headlineContent = { Text(text = "开源地址") }, supportingContent = {
                    Text(text = url)
                }, modifier = Modifier.clickable {
                    context.startActivity(
                        android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            android.net.Uri.parse(url)
                        )
                    )
                })
            }

            item {
                val url = "https://github.com/keluokeda/Hs/issues"
                ListItem(
                    headlineContent = { Text(text = "反馈问题和提出建议") },
                    modifier = Modifier.clickable {
                        context.startActivity(
                            android.content.Intent(
                                android.content.Intent.ACTION_VIEW,
                                android.net.Uri.parse(url)
                            )
                        )
                    })
            }
            item {
                ListItem(
                    headlineContent = { Text(text = "检查更新") },
                    trailingContent = {
                        Text(text = com.ke.hs.BuildConfig.VERSION_NAME)
                    },
                    modifier = Modifier.clickable {

                    })
            }

            item {
                ListItem(
                    headlineContent = { Text(text = "重置悬浮窗默认宽度") },
                    modifier = Modifier.clickable {
                        runBlocking {
                            context.setWindowWidth(context.resources.getDimensionPixelSize(R.dimen.module_floating_window_width))

                        }
                    })
            }

            item {
                ListItem(
                    headlineContent = { Text(text = "上报卡组数据") },
                    modifier = Modifier.clickable {
                        val fileString = FileTextProviderImpl(context).provide(HsLogFile.Deck)
                        throw RuntimeException(fileString ?: "fileString")
                    })
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun SettingsScreenPreview() {
    HsTheme {
        SettingsScreen()
    }
}