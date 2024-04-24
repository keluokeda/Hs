package com.ke.hs.ui.main.records

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ke.hs.db.entity.Game
import com.ke.hs.ui.GameRecordView


@Composable
fun RecordsRoute(list: List<Game>){
//    val viewModel = hiltViewModel<MainViewModel>()

//    val records by viewModel.records.collectAsState()

    RecordsScreen(list = list)
}

@Composable
private fun RecordsScreen(list: List<Game>){

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(list,{
            it.id
        }){
            GameRecordView(game = it)
        }
    }
}


