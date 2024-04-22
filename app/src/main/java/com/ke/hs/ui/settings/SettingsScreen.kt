package com.ke.hs.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import com.ke.hs.ui.theme.HsTheme


@Composable
fun SettingsRoute(onBack: () -> Unit = {}, toSync: () -> Unit = {}) {
    SettingsScreen(onBack, toSync)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(onBack: () -> Unit = {}, toSync: () -> Unit = {}) {
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