package com.ke.hs.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ke.hs.ui.chart.SummaryChartRoute
import com.ke.hs.ui.config.ConfigRoute
import com.ke.hs.ui.deck_detail.DeckDetailRoute
import com.ke.hs.ui.logs.LogsRoute
import com.ke.hs.ui.main.MainRoute
import com.ke.hs.ui.permissions.PermissionsRoute
import com.ke.hs.ui.settings.SettingsRoute
import com.ke.hs.ui.sync.SyncRoute
import com.tencent.bugly.crashreport.CrashReport

@Composable
internal fun NavigationTree(controller: NavHostController) {


    NavHost(navController = controller, startDestination = "/permissions") {


        composable("/permissions") {
            PermissionsRoute {


                controller.navigate(it) {
                    popUpTo("/permissions") {
                        inclusive = true
                    }
                }
            }
        }

        composable("/config") {
            ConfigRoute {
                controller.navigate("/sync") {
                    popUpTo("/config") {
                        inclusive = true
                    }
                }
            }
        }



        composable("/sync?canBack={canBack}", arguments = listOf(navArgument("canBack") {
            type = NavType.BoolType
            defaultValue = false
        })) {
            val canBack = it.arguments!!.getBoolean("canBack")
            SyncRoute(
                onBack = if (canBack) {
                    {
                        controller.popBackStack()
                    }
                } else {
                    null
                }
            ) {
                if (canBack) {
                    controller.popBackStack()
                } else {
                    controller.navigate("/main") {
                        popUpTo("/sync") {
                            inclusive = true
                        }
                    }
                }

            }


        }

        composable("/main") {
            MainRoute(toSummaryChart = {
                controller.navigate("/chart/summary")
            }, toSettings = {
                controller.navigate("/settings")
            }, toDeckDetail = { name, code ->
                controller.navigate("/deck/detail?name=$name&code=$code")
            })
        }


        composable("/chart/summary") {
            SummaryChartRoute {
                controller.popBackStack()
            }
        }

        composable("/settings") {
            SettingsRoute(onBack = {
                controller.popBackStack()
            }, toSync = {
                controller.navigate("/sync?canBack=true")
            }, toLogs = {
                controller.navigate("/logs")
            })
        }

        composable("/deck/detail?name={name}&code={code}") {
            DeckDetailRoute({
//                CrashReport.testJavaCrash()
                controller.popBackStack()
            })
        }

        composable("/logs") {
            LogsRoute {
                controller.popBackStack()
            }
        }
    }
}