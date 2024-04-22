package com.ke.hs.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ke.hs.R
import com.ke.hs.db.entity.Game
import com.ke.hs.entity.CardClass
import com.ke.hs.service.ComposeWindowService
import com.ke.hs.ui.theme.HsTheme


@Composable
fun MainRoute(toSummaryChart: () -> Unit = {}, toSettings: () -> Unit = {}) {
    val context = LocalContext.current

    val viewModel = hiltViewModel<MainViewModel>()
    val gameList by viewModel.gameList.collectAsState()
    MainScreen(gameList = gameList, start = {
        if (Settings.canDrawOverlays(context)) {
            context.startService(Intent(context, ComposeWindowService::class.java))
        } else {
            (context as Activity).startActivityForResult(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                ),
                101
            )
        }
    }, toSummaryChart = toSummaryChart, toSettings = toSettings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    gameList: List<Game> = emptyList(),
    start: () -> Unit = {},
    toSettings: () -> Unit = {},
    toSummaryChart: () -> Unit = {}
) {

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
    }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            items(gameList, {
                it.id
            }) {
                GameView(game = it)
            }
        }
    }
}

@Composable
private fun GameView(game: Game) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    (if (game.isUserWin == true) Color.Green else Color.Red)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Image(
                painter = painterResource(id = game.userHero?.roundIcon ?: R.drawable.neutral),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "VS",
                style = TextStyle(
                    color = Color.Yellow,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Image(
                painter = painterResource(id = game.opponentHero?.roundIcon ?: R.drawable.neutral),
                contentDescription = null,
                modifier = Modifier.size(40.dp)

            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = game.opponentName,
                style = TextStyle(color = Color.White),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            val duration = (game.endTime - game.startTime) / 1000


            Text(text = "${duration / 60}:${duration % 60}")
        }

        HorizontalDivider()
    }


}

@PreviewLightDark
@Composable
@Preview
private fun GameViewWinPreview() {
    HsTheme {
        GameView(
            game = Game(
                isUserWin = true,
                userHero = CardClass.Druid,
                opponentHero = CardClass.Hunter,
                opponentName = "汉库克"
            )
        )
    }
}

@Composable
@Preview
private fun GameViewLostPreview() {
    HsTheme {
        GameView(
            game = Game(
                isUserWin = false,
                userHero = CardClass.Druid,
                opponentHero = CardClass.Hunter,
                opponentName = "汉库克"
            )
        )
    }
}

@PreviewLightDark
@Composable
@Preview(showBackground = true)
private fun MainScreenPreview() {
    HsTheme {
        MainScreen(
            gameList = listOf(
                Game(
                    isUserWin = false,
                    userHero = CardClass.Druid,
                    opponentHero = CardClass.Hunter,
                    opponentName = "汉库克"
                ),
                Game(
                    isUserWin = true,
                    userHero = CardClass.Druid,
                    opponentHero = CardClass.Hunter,
                    opponentName = "汉库克"
                )
            )
        )
    }
}