package com.ke.hs.ui.sync

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ke.hs.module.api.HearthStoneJsonApi
import com.ke.hs.module.api.KeApi
import com.ke.hs.ui.theme.HsTheme
import kotlinx.coroutines.launch


@Composable
fun SyncRoute(onBack: (() -> Unit)? = null, next: () -> Unit) {
    val viewModel = hiltViewModel<SyncViewModel>()
    val loading by viewModel.loading.collectAsState()

    val scope = rememberCoroutineScope()

    SyncScreen(loading = loading, onBack = onBack) {
        scope.launch {
            if (viewModel.sync(it)) {
                next()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SyncScreen(loading: Boolean, onBack: (() -> Unit)? = null, sync: (Boolean) -> Unit) {
    var useKeApi by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "同步卡牌数据") },

            navigationIcon = if (onBack == null) {
                {}
            } else {
                {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }

            }
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(text = "数据来源：")

            ListItem(
                leadingContent = {
                    RadioButton(!useKeApi, onClick = {
                        useKeApi = false
                    })
                }, headlineContent = {
                    Text("官网")
                }, supportingContent = {
                    Text(HearthStoneJsonApi.BASE_URL)
                }
            )


            ListItem(
                leadingContent = {
                    RadioButton(useKeApi, onClick = {
                        useKeApi = true
                    }, enabled = false)
                }, headlineContent = {
                    Text("群主")
                }, supportingContent = {
                    Text(KeApi.baseUrl)
                }
            )


            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                sync(useKeApi)
            }, modifier = Modifier.fillMaxWidth(), enabled = !loading) {
                Text(text = "同步")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }

    }


}

@Composable
@PreviewLightDark
private fun SyncScreenPreview() {
    HsTheme {
        SyncScreen(loading = false) { }
    }
}