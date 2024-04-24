package com.ke.hs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ke.hs.R
import com.ke.hs.entity.Card
import com.ke.hs.entity.CardBean
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
fun CardView(card: CardBean) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(24.dp)) {
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
                        .background(Color.Black.copy(alpha = 0.6f))
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