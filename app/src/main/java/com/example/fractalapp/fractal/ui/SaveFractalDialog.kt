package com.example.fractalapp.fractal.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fractalapp.R
import com.example.fractalapp.fractal.FractalViewModel
import com.example.fractalapp.ui.theme.*

@Composable
fun SaveFractalDialog(
    onDismiss: () -> Unit,
    vm: FractalViewModel?
) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(modifier = Modifier
            .padding(15.dp)
            .background(
                color = BottomPanel,
                shape = RoundedCornerShape(WidgetCorner)
            )
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Сохранить фрактал в избранное",
                    style = TextStyle(
                        color = HintText,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = TextTitleSize
                    )
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Input(
                    modifier = Modifier.fillMaxWidth(),
                    value = vm!!.fractalName,
                    placeholder = "Название")
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Text(
                    text = "* При отсутствии названия фрактал будет сохранен под случайным номером",
                    style = TextStyle(
                        color = HintText,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = TextDescriptionSize
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
                
                vm.fractalImage.value?.let {
                    Box(modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center) {
                        Image(
                            modifier = Modifier.size(150.dp),
                            bitmap = it.asImageBitmap(),
                            contentScale = ContentScale.Fit,
                            contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Controllers
                        )
                    ) {
                        Text(text = "Отменить", style = TextStyle(
                            color = ButtonText,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontSize = TextButtonSize
                        ))
                    }

                    Button(
                        onClick = {
                            vm.saveFractal()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Controllers
                        )
                    ) {
                        Text(text = "ОК", style = TextStyle(
                            color = ButtonText,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontSize = TextButtonSize
                        ))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SaveFractalDialogPreview() {
    val ctx = LocalContext.current
    SaveFractalDialog(
        onDismiss = {},
        vm = null
    )
}