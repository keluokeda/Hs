package com.ke.hs.ui.config

import android.content.Context
import android.os.Environment
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ke.hs.FileService
import com.ke.hs.checkAppInstalled
import com.ke.hs.currentHsPackage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File

@Composable
internal fun ConfigRoute(next: () -> Unit) {
    ConfigScreen(next)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigScreen(next: () -> Unit = {}) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "写入配置文件") })
    }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "为了能让炉石传说App在游戏时生成日志文件，我们需要把以下内容写入到炉石传说app目录的log.config文件中")

            val context = LocalContext.current


            val text = context.assets.open("log.config").reader().readText()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        text = text,
                    )
                }

            }


            val scope = rememberCoroutineScope()

            var showDialog by remember {
                mutableStateOf(false)
            }


            Button(onClick = {
                if (!context.checkAppInstalled(runBlocking {
                        context.currentHsPackage.first()
                    })) {
                    showDialog = true
                    return@Button
                }

                scope.launch {
                    writeConfig(context, "log.config")
                    writeConfig(context, "client.config")
                    next()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "写入配置")
            }

            if (showDialog) {
                AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text(text = "知道了")
                    }
                }, title = {
                    Text(text = "提示")
                }, text = {
                    Text(text = "未安装炉石传说，请先安装炉石传说")
                })
            }
        }
    }
}

private suspend fun writeConfig(context: Context, fileName: String) {
    withContext(Dispatchers.IO) {
        val manager = FileService.getInstance() ?: return@withContext
        val path =
            Environment.getExternalStorageDirectory().path + "/Android/data/${context.currentHsPackage.first().packageName}/files/"

        val configFile = File(path, fileName)

        manager.deleteFile(configFile.path)
        val localConfigFile = File(context.getExternalFilesDir(null), fileName)
        localConfigFile.outputStream().use {
            context.assets.open(fileName).copyTo(it)
            it.flush()
        }
        manager.copyFile(localConfigFile.path, configFile.path)
    }
}