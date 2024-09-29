package com.example.fractalapp.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fractalapp.R
import com.example.fractalapp.ui.theme.*

@Composable
fun MenuWidgetWithDescription(
    text: String,
    description: String,
    imgResource: Int,
    firstColor: Color = Color(0xFF097E66),
    secondColor: Color = Color(0xFF1D2B53),
    imgTint: Color = firstColor,
    onClick: ()->Unit = {}
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(WidgetCorner),
    ) {

        Box(
            Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            secondColor,
                            firstColor,
                        ),
                    )
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        color = Ripple,
                    )
                ) {
                    onClick()
                }
        ) {


            Box(modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {

                Image (
                    modifier = Modifier
                        .size(200.dp)
                        .padding(start = 20.dp),
                    imageVector = ImageVector.vectorResource(imgResource),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(imgTint)
                )
            }


            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = text,
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.montserrat_bold)
                            ),
                            fontSize = TextWidgetTitleSize,
                            color = WidgetText,
                        ),
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 5.dp, bottom = 10.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = description,
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.montserrat_regular)
                            ),
                            fontSize = TextDescriptionSize,
                            color = SubTitlesText,
                        ))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }


        }

    }
}

@Preview
@Composable
fun MenuWidgetWithDescrPreview() {
    MenuWidgetWithDescription(
        text = "Создать фрактал",
        description = "Фракталы L-системы, основанны на рекурсивных правилах",
        R.drawable.play
    )
}