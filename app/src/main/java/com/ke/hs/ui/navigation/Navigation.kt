package com.ke.hs.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ke.hs.ui.chart.SummaryChartRoute
import com.ke.hs.ui.config.ConfigRoute
import com.ke.hs.ui.main.MainRoute
import com.ke.hs.ui.permissions.PermissionsRoute
import com.ke.hs.ui.sync.SyncRoute

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



        composable("/sync") {
            SyncRoute {
                controller.navigate("/main") {
                    popUpTo("/sync") {
                        inclusive = true
                    }
                }
            }
        }

        composable("/main") {
            MainRoute(toSummaryChart = {
                controller.navigate("/chart/summary")
            })
        }


        composable("/chart/summary") {
            SummaryChartRoute {
                controller.popBackStack()
            }
        }
    }
}