package com.ke.hs.ui.deck_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ke.hs.R
import com.ke.hs.db.entity.Game
import com.ke.hs.entity.Card
import com.ke.hs.entity.CardBean
import com.ke.hs.entity.CardClass
import com.ke.hs.renderImage
import com.ke.hs.ui.CardView
import com.ke.hs.ui.GameRecordView
import com.ke.hs.ui.theme.HsTheme


@Composable
fun DeckDetailRoute(onBack: () -> Unit = {}) {
    val viewModel = hiltViewModel<DeckDetailViewModel>()

    val deckCardBeanList by viewModel.deckCardBeanList.collectAsState()

    val records by viewModel.records.collectAsState()

    val statistics by viewModel.statistics.collectAsState()

    DeckDetailScreen(
        title = viewModel.name,
        code = viewModel.code,
        onBack = onBack,
        deckCardBeanList,
        records = records,
        onDelete = {
            viewModel.deleteGameRecord(it)
        },
        statistics = statistics
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeckDetailScreen(
    title: String,
    code: String,
    onBack: () -> Unit,
    deckCardBeanList: List<CardBean>,
    records: List<Game>,
    onDelete: (Game) -> Unit,
    statistics: List<Triple<CardClass, Int, Int>>
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = title) }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            var selectedTabIndex by remember {
                mutableIntStateOf(1)
            }

            TabRow(selectedTabIndex = selectedTabIndex) {

                TabItem.entries.forEachIndexed { index, tabItem ->
                    Tab(selected = index == selectedTabIndex, onClick = {
                        selectedTabIndex = index
                    }, text = {
                        Text(text = tabItem.label)
                    })
                }
            }



            Box(modifier = Modifier.weight(1f)) {
                when (selectedTabIndex) {
                    0 -> {
                        DeckCardsView(deckCardBeanList, code)
                    }

                    2 -> {
                        RecordsView(records = records, onDelete = onDelete)
                    }

                    1 -> {
                        DeckStatisticsView(list = statistics)
                    }
                }


            }
        }


    }
}


@Composable
@PreviewLightDark
private fun DeckStatisticsViewPreview() {
    HsTheme {
        DeckStatisticsView(
            list = listOf(
                Triple(CardClass.DeathKnight, 10, 10),
                Triple(CardClass.Mage, 5, 10),

                )
        )
    }
}

@Composable
private fun DeckStatisticsView(list: List<Triple<CardClass, Int, Int>>) {
    LazyColumn {
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(

                    modifier = Modifier.size(40.dp)
                )

                Text(
                    text = "对战场数",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "胜利",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "失败",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "胜率",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        items(list) {


            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = it.first.roundIcon ?: R.drawable.neutral),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )

                Text(
                    text = (it.second + it.third).toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = (it.second).toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = (it.third).toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = ((it.second * 1.0f / (it.second + it.third) * 100f)).toString()
                        .substringBefore(".") + "%",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "总计",
                    textAlign = TextAlign.Center,

                    modifier = Modifier
                        .size(40.dp)
                        .wrapContentHeight()
                )

                Text(
                    text = list.sumOf { it.second + it.third }.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = list.sumOf { it.second }.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = list.sumOf { it.third }.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = (list.sumOf { it.second } * 1.0f / list.sumOf { it.second + it.third }).times(
                        100
                    ).toString().substringBefore(".") + "%",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RecordsView(records: List<Game>, onDelete: (Game) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(records) {
            GameRecordView(game = it, onDelete = onDelete)
        }
    }
}

@Composable
private fun DeckCardsView(
    deckCardBeanList: List<CardBean>,
    code: String
) {
    var selectedCard by remember {
        mutableStateOf<Card?>(null)
    }
    LazyColumn {

        item {
            ListItem(headlineContent = {
                Text(text = code)
            }, trailingContent = {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
            }, modifier = Modifier.clickable {

            })
        }

        items(deckCardBeanList) {
            CardView(card = it, height = 48.dp) { card ->
                selectedCard = card
            }
        }
    }

    if (selectedCard != null) {
        Dialog(onDismissRequest = { selectedCard = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.0f / 1.5f)
            ) {
                AsyncImage(
                    model = selectedCard?.id?.renderImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,

                    )
            }
        }
    }
}


private enum class TabItem(val label: String) {
    Cards("卡牌"),
    Statistics("统计"),
    Records("记录")
}
