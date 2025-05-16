package com.ke.hs.ui.permissions

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.hs.currentHsPackage
import com.ke.hs.module.entity.HsPackage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File


@Composable
internal fun PermissionsRoute(next: (String) -> Unit) {
    val viewModel = hiltViewModel<PermissionsViewModel>()

    val currentHsPackage by viewModel.currentHsPackage.collectAsState()

    val hasPermission by viewModel.hasPermission.collectAsStateWithLifecycle()

    LocalLifecycleOwner.current.lifecycle.addObserver(viewModel)

    val context = LocalContext.current
    PermissionsScreen(currentHsPackage, {
        viewModel.setCurrentHsPackage(it)
    }, hasPermission) {

        val hsPackageName = runBlocking {
            context.currentHsPackage.first().packageName
        }

        val zeroWidthSpace = "\u200b"


        val path =
            "/storage/emulated/0/${zeroWidthSpace}Android/data/${hsPackageName}/files/"
//            Environment.getExternalStorageDirectory().path + "/Android/data/${hsPackageName}/files/"
        val listFiles = File(path).listFiles()?.map { it.path } ?: emptyList()

//        val files = FileService.getInstance()!!.getFiles(path)
        if (!listFiles.contains(path + "log.config") || !listFiles.contains(path + "client.config")) {
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
private fun PermissionsScreen(
    currentHsPackage: HsPackage,
    setCurrentHsPackage: (HsPackage) -> Unit,
    hasPermission: Boolean,
    next: suspend () -> Unit = {}
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("存储授权") })
    }) { paddingValues ->

        val context = LocalContext.current

        ///是否安装


        Column(modifier = Modifier.padding(paddingValues)) {
//            val context = LocalContext.current

            ListItem(
                headlineContent = {
                    Text("授予App存储访问权限")
                }, modifier = Modifier.clickable(enabled = !hasPermission, onClick = {
                    val action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    context.startActivity(Intent(action))
                })
            )


            ListItem(headlineContent = {
                Text("炉石来源")
            }, trailingContent = {
                TextButton(onClick = {


                    val hsPackage = findHsPackage(context)
                    if (hsPackage == null) {
                        Toast.makeText(context, "未发现炉石app", Toast.LENGTH_SHORT).show()

                    } else {
                        setCurrentHsPackage(hsPackage)
                    }

                }) {
                    Text("自动检测")
                }
            })

            HsPackage.entries.forEach {
                ListItem(
                    headlineContent = {
                        Text(it.description)
                    },
                    leadingContent = {
                        RadioButton(
                            selected = it == currentHsPackage,
                            onClick = {
                                setCurrentHsPackage(it)
                            }
                        )
                    }
                )
            }

            val scope = rememberCoroutineScope()

            Button(
                onClick = {
                    scope.launch {
                        next()
                    }
                },
                enabled = hasPermission,
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

private fun findHsPackage(context: Context): HsPackage? {
    HsPackage.entries.forEach {
        try {
            context.packageManager.getPackageInfo(it.packageName, 0)
            return it
        } catch (e: Exception) {

        }

    }

    return null
}



