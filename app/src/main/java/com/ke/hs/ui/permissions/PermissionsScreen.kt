package com.ke.hs.ui.permissions

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ke.hs.FileService
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku


@Composable
internal fun PermissionsRoute(next: (String) -> Unit) {
    val viewModel = hiltViewModel<PermissionsViewModel>()
    PermissionsScreen {
        val path =
            Environment.getExternalStorageDirectory().path + "/Android/data/com.blizzard.wtcg.hearthstone/files/"

        val files = FileService.getInstance()!!.getFiles(path)
        if (!files.contains(path + "log.config") || !files.contains(path + "client.config")) {
            //如果没有写入配置文件
            next("/config")
        } else if (viewModel.hasCardData()) {
            next("/main")
        } else {
            next("/sync")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PermissionsScreen(next: suspend () -> Unit = {}) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Android14 Shizuku授权") })
    }) { paddingValues ->

        val context = LocalContext.current

        ///是否安装
        var installed by remember { mutableStateOf(isShizukuInstalled(context)) }

        var canUse by remember {
            mutableStateOf(Shizuku.pingBinder())
        }

        var auth by remember {
            mutableStateOf(hasPermission())
        }


        var serviceStarted by remember {
            mutableStateOf(FileService.getInstance() != null)
        }

        Column(modifier = Modifier.padding(paddingValues)) {

            ListItem(headlineContent = {
                Text("1，安装Shizuku")
            }, trailingContent = {
                Icon(
                    imageVector = if (installed) Icons.Default.Done else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }, modifier = Modifier.clickable {
                installed = isShizukuInstalled(context)
                if (!installed) {
                    AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("请先安装Shizuku")
                        .setNeutralButton("打开浏览器下载") { _, _ ->
                            //打开浏览器下载
                            val url = "https://shizuku.rikka.app/zh-hans/"
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

                        }

                        .setPositiveButton("确定") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            })
            ListItem(headlineContent = {
                Text("2，Shizuku是否可用")
            }, trailingContent = {
                Icon(
                    imageVector = if (canUse) Icons.Default.Done else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }, modifier = Modifier.clickable {
                canUse = Shizuku.pingBinder()
                if (!canUse) {
                    AlertDialog.Builder(context)
                        .setTitle("Shizuku不可用")
                        .setMessage("请检查Shizuku是否可用")
                        .setPositiveButton("确定") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            })
            ListItem(headlineContent = {
                Text("3，Shizuku授权")
            }, trailingContent = {
                Icon(
                    imageVector = if (auth) Icons.Default.Done else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }, modifier = Modifier.clickable {
                auth =
                    hasPermission()
                if (!auth) {
                    try {
                        Shizuku.requestPermission(100)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

            ListItem(headlineContent = {
                Text("4，启动服务")
            }, trailingContent = {
                Icon(
                    imageVector = if (serviceStarted) Icons.Default.Done else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }, modifier = Modifier.clickable {
                serviceStarted =
                    FileService.getInstance() != null
                if (!serviceStarted) {
                    FileService.bindService(context)
                }
            })

            val scope = rememberCoroutineScope()

            Button(
                onClick = {
                    scope.launch {
                        next()
                    }
                },
                enabled = installed && canUse && auth && serviceStarted,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("下一步")
            }
        }
    }

}

private fun isShizukuInstalled(context: Context): Boolean {
    try {
        context.packageManager.getPackageInfo("moe.shizuku.privileged.api", 0)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return false
}

private fun hasPermission(): Boolean {
    return try {
        Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}