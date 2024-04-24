package com.ke.hs.ui.logs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.ke.hs.logsEnable
import com.ke.hs.setLogsEnable
import com.ke.hs.ui.theme.HsTheme
import kotlinx.coroutines.launch


@Composable
fun LogsRoute(onBack: () -> Unit) {
    val context = LocalContext.current

    val enable by context.logsEnable.collectAsState(initial = false)

    val scope = rememberCoroutineScope()

    LogsScreen(onBack = onBack, logsEnable = enable) {
        scope.launch {
            context.setLogsEnable(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LogsScreen(onBack: () -> Unit, logsEnable: Boolean, updateEnable: (Boolean) -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "日志管理") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                ListItem(headlineContent = { Text(text = "保存日志文件") }, trailingContent = {
                    Switch(checked = logsEnable, onCheckedChange = updateEnable)
                })
            }
        }

    }

}


@Composable
@PreviewLightDark
private fun LogsScreenPreview() {
    HsTheme {
        LogsScreen(onBack = { /*TODO*/ }, logsEnable = false) {

        }
    }
}