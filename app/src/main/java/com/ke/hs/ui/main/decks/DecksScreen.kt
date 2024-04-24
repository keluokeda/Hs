package com.ke.hs.ui.main.decks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ke.hs.tileImage
import com.ke.hs.ui.main.DeckSummary
import com.ke.hs.ui.main.MainViewModel
import com.ke.hs.ui.theme.HsTheme


@Composable
fun DecksRoute(list: List<DeckSummary>) {
//    val viewModel = hiltViewModel<MainViewModel>()
//    val list by viewModel.deckSummaryList.collectAsState()

    DecksScreen(list = list)
}

@Composable
private fun DecksScreen(list: List<DeckSummary>) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(list) {

            DeckSummaryView(it)

        }
    }
}

@Composable
private fun DeckSummaryView(it: DeckSummary) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        AsyncImage(
            model = it.heroId.tileImage,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(horizontal = 16.dp)

            ,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = it.name, color = Color.White)

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "胜利：${it.winCount}，失败：${it.lostCount}，胜率：${it.percent()}",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun DeckSummaryViewPreview() {
    HsTheme {
        DeckSummaryView(it = DeckSummary("汉库克", "", 10, 10, "HERO_11b"))
    }
}