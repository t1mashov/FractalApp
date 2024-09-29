package com.example.fractalapp.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fractalapp.R
import com.example.fractalapp.db.BitmapConverter
import com.example.fractalapp.db.Fractal
import com.example.fractalapp.home.FractalListWidgetViewModel
import com.example.fractalapp.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FractalWidget(
    fractal: Fractal,
    navController: NavHostController?,
    vm: FractalListWidgetViewModel,
    modifier: Modifier = Modifier
) {

    val bmp = BitmapConverter.toBitmap(fractal.icon)

    val selection = remember { vm.isSelectionExists() }
    val selectionId = remember { vm.selectionId }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor =
                if (selectionId.value != fractal.id) WidgetFractalSampleBackground
                else WidgetFractalSampleBackgroundLongPressed
        ),
        shape = RoundedCornerShape(WidgetCorner)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (!selection.value)
                            vm.onClick(navController!!, fractal.id)
                    },
                    onLongClick = {
                        vm.onLongPress(navController!!, fractal.id)
                    },
                    indication = rememberRipple( color = Color.White ),
                    interactionSource = remember { MutableInteractionSource() }
                )

        ) {

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                bmp?.let {
                    Image(
                        modifier = Modifier
                            .size(150.dp, 150.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = fractal.title,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.montserrat_regular)
                        ),
                        fontSize = 20.sp,
                        color = WidgetText,
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
fun FractalSamplePreview() {

    val ctx = LocalContext.current

//    FractalSampleWidget(
//        fractal = Fractal(
//            id = 0,
//            icon = BitmapConverter.fromBitmap()),
//        navController = null
//    )
}
