package com.ke.hs.ui.logs

import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.content.FileProvider
import com.ke.hs.logsEnable
import com.ke.hs.setLogsEnable
import com.ke.hs.ui.theme.HsTheme
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun LogsRoute(onBack: () -> Unit) {
    val context = LocalContext.current


    val refreshLogsFileList = {
        File(context.getExternalFilesDir(null), "logs").listFiles()?.sortedBy {
            -it.lastModified()
        }?.map {
            it.name to it.path
        } ?: emptyList()
    }

    var logFileList by remember {
        mutableStateOf(refreshLogsFileList())
    }

    val enable by context.logsEnable.collectAsState(initial = false)

    val scope = rememberCoroutineScope()

    LogsScreen(onBack = onBack, logsEnable = enable, logFileList = logFileList, onDelete = {
        File(it).delete()
        logFileList = refreshLogsFileList()
    }, onShare = {
        val file = File(it)
        val uri = FileProvider.getUriForFile(context, context.packageName + ".share", file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setType("text/plain")
        context.startActivity(Intent.createChooser(intent, "分享到："))
//        intent.addFlags()
    }) {
        scope.launch {
            context.setLogsEnable(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LogsScreen(
    onBack: () -> Unit,
    logsEnable: Boolean,
    logFileList: List<Pair<String, String>> = emptyList(),
    onDelete: (String) -> Unit = {},
    onShare: (String) -> Unit = {},
    updateEnable: (Boolean) -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "日志管理") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
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

            items(logFileList, {
                it
            }) {

                ListItem(headlineContent = { Text(text = it.first) }, trailingContent = {
                    Row {
                        IconButton(onClick = {
                            onDelete(it.second)
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }

                        IconButton(onClick = {
                            onShare(it.second)
                        }) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null)
                        }
                    }
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