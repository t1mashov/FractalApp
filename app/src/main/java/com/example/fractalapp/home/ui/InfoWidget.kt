package com.example.fractalapp.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fractalapp.R
import com.example.fractalapp.ui.theme.FractalTheme


@Composable
fun InfoWidget(
    modifier: Modifier = Modifier,
    text: String,
    description: String,
    color: Color = Color(0xFF94563A),
    onClick: () -> Unit = {}
//    firstColor: Color = Color(0xFF8D8826),
//    secondColor: Color = Color(0xFF6B412E)
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(FractalTheme.WidgetCorner),
    ) {

        Box(
            Modifier
                .fillMaxWidth()
                .background(color = color)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        color = FractalTheme.Ripple,
                    )
                ) {
                    onClick()
                }
        ) {

            Column(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = text,
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.montserrat_bold)
                            ),
                            fontSize = FractalTheme.TextWidgetTitleSize,
                            color = FractalTheme.WidgetText,
                        ),
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Text(
                        text = description,
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.montserrat_regular)
                            ),
                            fontSize = FractalTheme.TextDescriptionSize,
                            color = FractalTheme.SubTitlesText,
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
fun InfoWidgetPreview() {
    InfoWidget(
        text = "Правила L-системы",
        description = "Подробный разбор структуры L-систем с примерами",
    )
}