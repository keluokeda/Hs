package com.ke.hs.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.ke.hs.demo.ui.theme.HsTheme
import com.ke.hs.module.parser.PowerParser
import com.ke.hs.module.parser.PowerTagHandler
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var powerTagHandler: PowerTagHandler

    @Inject
    lateinit var powerParser: PowerParser


    private var index = 0

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val lines = assets.open("zha.log").bufferedReader().readLines().toMutableList()



        powerParser.powerTagListener = {
            lifecycleScope.launch {
                powerTagHandler.handle(it)
            }
        }



        setContent {
            HsTheme {
                Scaffold(topBar = {
                    TopAppBar(title = { Text(text = "Demo") })
                }) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {


                        LaunchedEffect(key1 = Unit) {
                            powerTagHandler.gameEventFlow.collect {
//                                Logger.d("$index event = $it")
                            }
                        }

                        ListItem(
                            headlineContent = { Text(text = "读取") },
                            modifier = Modifier.clickable {


                                repeat(1000) {
                                    index++
                                    val line = lines.removeFirstOrNull()
                                    if (line != null) {
                                        powerParser.parse(line)
                                    }
                                }
                            })

                    }

                }
            }
        }
    }
}
