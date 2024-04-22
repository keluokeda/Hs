package com.ke.hs.ui.sync

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch


@Composable
fun SyncRoute(next: () -> Unit) {
    val viewModel = hiltViewModel<SyncViewModel>()
    val loading by viewModel.loading.collectAsState()

    val scope = rememberCoroutineScope()

    SyncScreen(loading = loading) {
        scope.launch {
            viewModel.sync()
            next()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SyncScreen(loading: Boolean, sync: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "同步卡牌数据") })
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(text = "数据来源：https://hearthstonejson.com")
            Button(onClick = sync, modifier = Modifier.fillMaxWidth(), enabled = !loading) {
                Text(text = "同步")
            }
        }

    }
}