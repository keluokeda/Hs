package com.ke.hs.service

import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.ke.hs.R
import com.ke.hs.lastWindowWidth
import com.ke.hs.module.parser.DeckCardObserver
import com.ke.hs.setWindowWidth
import com.ke.hs.ui.CardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ComposeWindowService : LifecycleService(), SavedStateRegistryOwner {


    private lateinit var frameLayout: FrameLayout
    private val windowManager: WindowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }


    @Inject
    lateinit var deckCardObserver: DeckCardObserver


    private var show = true

    private fun showView() {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

        layoutParams.width =
            runBlocking {
                lastWindowWidth.first()
                    ?: resources.getDimension(R.dimen.module_floating_window_width).toInt()
            }

        layoutParams.height = resources.getDimension(R.dimen.module_floating_window_height).toInt()
        //需要设置 这个 不然空白地方无法点击
        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.gravity = Gravity.START or Gravity.TOP
        layoutParams.format = PixelFormat.RGBA_8888//防止文字模糊

        savedStateRegistryController.performAttach() // you can ignore this line, becase performRestore method will auto call performAttach() first.
        savedStateRegistryController.performRestore(null)


        frameLayout = LayoutInflater.from(this).inflate(R.layout.compose_window, null)
            .findViewById(R.id.frame_layout)

        val bottom1 = frameLayout.findViewById<View>(R.id.bottom1)
        val bottom2 = frameLayout.findViewById<View>(R.id.bottom2)
        frameLayout.setViewTreeLifecycleOwner(this)
        frameLayout.setViewTreeSavedStateRegistryOwner(this)
        val composeView = ComposeView(this)
        frameLayout.addView(composeView, 0)
//        frameLayout.setPadding(0, 0, 16, 16)
        bottom1.setOnTouchListener(ScaleTouchListener(windowManager, frameLayout, layoutParams) {
            lifecycleScope.launch {
                this@ComposeWindowService.setWindowWidth(it)
            }
        })
        bottom2.setOnTouchListener(ScaleTouchListener(windowManager, frameLayout, layoutParams) {
            lifecycleScope.launch {
                this@ComposeWindowService.setWindowWidth(it)
            }
        })

        composeView.setViewTreeSavedStateRegistryOwner(this)
        composeView.setViewTreeLifecycleOwner(this)

        composeView.setContent {
            FloatingComposeView(deckCardObserver = deckCardObserver, exitApp = {
//                exitProcess(0)
                stopSelf()
            }, toggle = {
                show = !show
                layoutParams.height = if (show) {
                    resources.getDimension(R.dimen.module_floating_window_height).toInt()
                } else {
                    resources.getDimension(R.dimen.module_floating_window_header_height).toInt()

                }
                windowManager.updateViewLayout(frameLayout, layoutParams)
            })
        }

        composeView.setOnTouchListener(
            ItemViewTouchListener(
                layoutParams,
                windowManager,
                frameLayout
            )
        )

//        bottom2.setOnTouchListener(
//            ItemViewTouchListener(
//                layoutParams,
//                windowManager,
//                frameLayout
//            )
//        )
        windowManager.addView(frameLayout, layoutParams)


    }

    override fun onCreate() {
        super.onCreate()
        deckCardObserver.init(lifecycleScope)

        showView()


    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(frameLayout)
    }


    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry


}

@Composable
private fun FloatingComposeView(
    deckCardObserver: DeckCardObserver,
    exitApp: () -> Unit,
    toggle: () -> Unit = {}
) {


    MaterialTheme {

        var cardListType by remember {
            mutableStateOf(CardListType.DECK)
        }


        var showAnalytics by remember {
            mutableStateOf(false)
        }

        var analyticsText by remember {
            mutableStateOf("")
        }

        val cardList by if (cardListType == CardListType.DECK) {
            deckCardObserver.deckCardList.collectAsState()
        } else if (cardListType == CardListType.MY_GRAVEYARD) {
            deckCardObserver.userGraveyardCardList.collectAsState()
        } else {
            deckCardObserver.opponentGraveyardCardList.collectAsState()
        }


        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .padding(vertical = 5.dp, horizontal = 32.dp)
                    .background(Color.White, shape = RoundedCornerShape(2.dp))

            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.module_floating_window_header_height))
                    .background(
                        Color.Black.copy(
                            alpha = 0.3f
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
//                IconButton(onClick = exitApp) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Default.ExitToApp,
//                        contentDescription = null,
//                        tint = Color.Red
//                    )
//                }

//                IconButton(onClick = {
//                    analyticsText = deckCardObserver.analytics()
//                    showAnalytics = !showAnalytics
//
//                }) {
//                    Icon(
//                        imageVector = Icons.Default.Analytics,
//                        contentDescription = null,
//                        tint = Color.White
//                    )
//                }

                IconButton(onClick = {
                    toggle()
                }) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Box {
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text(text = "牌库") }, onClick = {
                            cardListType = CardListType.DECK
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text(text = "自己墓地") }, onClick = {
                            cardListType = CardListType.MY_GRAVEYARD
                            expanded = false
                        })

                        DropdownMenuItem(text = { Text(text = "对手墓地") }, onClick = {
                            cardListType = CardListType.OPPONENT_GRAVEYARD
                            expanded = false
                        })

                    }
                }

                Box {
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text(text = "退出") }, onClick = {
                            exitApp()
                            expanded = false
                        })

                        DropdownMenuItem(
                            text = { Text(text = if (showAnalytics) "隐藏诊断" else "显示诊断") },
                            onClick = {
                                analyticsText = deckCardObserver.analytics()
                                showAnalytics = !showAnalytics
                                expanded = false
                            })

                    }
                }


            }

            if (showAnalytics) {
                Text(text = analyticsText, style = TextStyle(color = Color.White))
            } else {

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            Color.Black.copy(alpha = 0.5f)
                        )
                ) {
                    items(cardList) { card ->
                        CardView(card)
                    }
                }
            }


        }
    }

}

enum class CardListType {
    DECK, MY_GRAVEYARD, OPPONENT_GRAVEYARD
}
