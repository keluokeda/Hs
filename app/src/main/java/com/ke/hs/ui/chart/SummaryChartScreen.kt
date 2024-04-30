package com.ke.hs.ui.chart

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.ke.hs.module.entity.CardClass
import com.ke.hs.ui.theme.HsTheme

@Composable
fun SummaryChartRoute(onBack: () -> Unit) {
    val viewModel = hiltViewModel<SummaryChartViewModel>()
    val opponentHeroCount by viewModel.opponentHeroCount.collectAsState()

    SummaryChartScreen(onBack, opponentHeroCount)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SummaryChartScreen(
    onBack: () -> Unit = {},
    opponentHeroCount: List<Pair<CardClass, Int>> = emptyList()
) {

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "总览") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(
                    paddingValues
                )
                .padding(16.dp)
        ) {

            item {
                Text(text = "对手职业分布")
            }

            item {
                OpponentHeroCountChart(opponentHeroCount)
            }
        }
    }
}

@Composable
private fun OpponentHeroCountChart(opponentHeroCount: List<Pair<CardClass, Int>>) {

    if (opponentHeroCount.isEmpty()) {
        return
    }

    val pieChartData = opponentHeroCount.map {
        PieChartData.Slice(
            stringResource(id = it.first.titleRes),
            it.second.toFloat(),
            colorResource(id = it.first.color)
        )
    }

    PieChart(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        pieChartData = PieChartData(pieChartData, PlotType.Pie),
        pieChartConfig = PieChartConfig(
            labelVisible = true,
            backgroundColor = Color.Transparent,
        )
    )
}

@Composable
@Preview
private fun OpponentHeroCountChartPreview() {
    HsTheme {
        OpponentHeroCountChart(
            opponentHeroCount = listOf(
                CardClass.Mage to 10,
                CardClass.DeathKnight to 20
            )
        )
    }
}

@Composable
@Preview
@PreviewLightDark
private fun SummaryChartScreenPreview() {
    HsTheme {
        SummaryChartScreen(
            opponentHeroCount = listOf(
                CardClass.Mage to 10,
                CardClass.DeathKnight to 20
            )
        )
    }
}