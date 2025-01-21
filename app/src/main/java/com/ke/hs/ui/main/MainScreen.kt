package com.ke.hs.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ke.hs.module.db.entity.Game
import com.ke.hs.service.ComposeWindowService
import com.ke.hs.ui.main.decks.DecksRoute
import com.ke.hs.ui.main.records.RecordsRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRoute(
    toSummaryChart: () -> Unit = {},
    toSettings: () -> Unit = {},
    toDeckDetail: (String, String) -> Unit
) {
    val context = LocalContext.current

    val viewModel = hiltViewModel<MainViewModel>()

    val records by viewModel.records.collectAsState()
    val deckSummaryList by viewModel.deckSummaryList.collectAsState()

    var showFloatingWindowPermissionDialog by remember { mutableStateOf(false) }
    MainScreen(
        start = {
            if (Settings.canDrawOverlays(context)) {
                context.startService(Intent(context, ComposeWindowService::class.java))
            } else {
                showFloatingWindowPermissionDialog = true
            }
        },
        toSummaryChart = toSummaryChart,
        toSettings = toSettings,
        toDeckDetail = toDeckDetail,
        records = records,
        deckSummaryList = deckSummaryList
    )

    if (showFloatingWindowPermissionDialog) {
        AlertDialog(onDismissRequest = {
            showFloatingWindowPermissionDialog = false
        }, confirmButton = {
            TextButton(onClick = {
                (context as Activity).startActivityForResult(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    ),
                    101
                )
                showFloatingWindowPermissionDialog = false
            }) {
                Text("去打开")
            }
        }, dismissButton = {
            TextButton(onClick = {
                showFloatingWindowPermissionDialog = false
            }) {
                Text("取消")
            }
        }, title = {
            Text("提示")
        }, text = {
            Text("请为炉石记牌器打开悬浮窗权限")
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    start: () -> Unit = {},
    toSettings: () -> Unit = {},
    toSummaryChart: () -> Unit = {},
    toDeckDetail: (String, String) -> Unit,
    records: List<Game>,
    deckSummaryList: List<DeckSummary>
) {
    val navController = rememberNavController()


    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "首页") }, actions = {
            IconButton(onClick = toSummaryChart) {
                Icon(imageVector = Icons.Default.PieChart, contentDescription = null)
            }
            IconButton(onClick = toSettings) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = start) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
        }
    }, bottomBar = {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            MainScreen.entries.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.imageVector, contentDescription = null) },
                    label = { Text(item.label) },
                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }) { padding ->
        NavHost(
            navController,
            startDestination = MainScreen.Records.route,
            Modifier.padding(padding)
        ) {


            composable(MainScreen.Records.route) {
                RecordsRoute(records)
            }
            composable(MainScreen.Decks.route) {
//                RecordsRoute()
                DecksRoute(deckSummaryList, toDeckDetail = toDeckDetail)
            }
        }
    }
}

private enum class MainScreen(val imageVector: ImageVector, val label: String, val route: String) {

    Records(Icons.Default.Home, "记录", "/main/records"),
    Decks(Icons.AutoMirrored.Filled.List, "卡组", "/main/decks")
}
