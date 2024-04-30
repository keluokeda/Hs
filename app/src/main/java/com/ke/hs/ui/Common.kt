package com.ke.hs.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ke.hs.R
import com.ke.hs.module.db.entity.Game
import com.ke.hs.module.entity.Card
import com.ke.hs.module.entity.CardBean
import com.ke.hs.module.entity.CardClass
import com.ke.hs.ui.theme.HsTheme
import com.orhanobut.logger.Logger


@PreviewLightDark
@Composable
private fun CardViewPreview() {
    CardView(
        card = CardBean(
            Card(name = "麻风侏儒", dbfId = 1, id = ""),
            count = 2
        )
    )
}

@Composable
fun CardView(card: CardBean, height: Dp = 24.dp, onClick: (Card) -> Unit = {}) {
    Column(modifier = Modifier.clickable {
        onClick(card.card)
    }) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(height)) {
            Text(
                text = card.card.cost.toString(),
                style = TextStyle(color = Color.White, textAlign = TextAlign.Center),

                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(Color.DarkGray)
                    .wrapContentHeight()
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {


                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = "https://art.hearthstonejson.com/v1/tiles/${card.card.id}.png",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onError = {
                        Logger.d("$it")
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card.card.name, style = TextStyle(
                            color = Color.White
                        ), modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )

                    Text(
                        text = card.count.toString(),
                        style = TextStyle(color = Color.White, textAlign = TextAlign.Center),
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .background(
                                colorResource(
                                    id = card.card.rarity?.colorRes ?: R.color.module_mage
                                )
                            )
                            .wrapContentHeight()
                    )
                }

            }


        }

        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp
        )
    }
}


@Composable
fun GameRecordView(game: Game, onDelete: (Game) -> Unit = {}) {
    var showDialog by remember {

        mutableStateOf(false)
    }

    Column(modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onLongPress = {
                showDialog = true
            }
        )
    }) {
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
                painter = painterResource(id = game.userHero?.roundIcon ?: com.ke.hs.module.R.drawable.neutral),
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
                painter = painterResource(id = game.opponentHero?.roundIcon ?: com.ke.hs.module.R.drawable.neutral),
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


    if (showDialog) {
        AlertDialog(onDismissRequest = {
            showDialog = false
        }, confirmButton = {
            TextButton(onClick = {
                showDialog = false
                onDelete(game)
            }) {
                Text(text = "删除")
            }
        }, title = {
            Text(text = "提示")
        }, text = {
            Text(text = "确定删除该条记录吗？")
        }, dismissButton = {
            TextButton(onClick = { showDialog = false }) {
                Text(text = "取消")
            }
        })
    }

}

@PreviewLightDark
@Composable
private fun GameViewWinPreview() {
    HsTheme {
        GameRecordView(
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
@PreviewLightDark
private fun GameViewLostPreview() {
    HsTheme {
        GameRecordView(
            game = Game(
                isUserWin = false,
                userHero = CardClass.Druid,
                opponentHero = CardClass.Hunter,
                opponentName = "汉库克"
            )
        )
    }
}