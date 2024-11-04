package com.example.fractalapp.fractal.ui

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fractalapp.App
import com.example.fractalapp.R
import com.example.fractalapp.fractal.FractalViewModel
import com.example.fractalapp.fractal.model.FractalResult
import com.example.fractalapp.ui.theme.FractalTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPanel(
    vm: FractalViewModel,
    state: BottomSheetScaffoldState?,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val rules = rememberSaveable { vm.rules }
    val axiom = rememberSaveable { vm.axiom }
    val angle = rememberSaveable { vm.angle }
    val gens = rememberSaveable { vm.gens }
    val step = rememberSaveable { vm.step }

    val useColors = remember { vm.useColors }
    val resCodes = remember { vm.resCodes }

    val redChannel = remember { vm.redChannel }
    val greenChannel = remember { vm.greenChannel }
    val blueChannel = remember { vm.blueChannel }

    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxWidth()
    ) {

        Input(
            modifier = Modifier.fillMaxWidth(),
            value = rules,
            placeholder = "Правила",
            keyboardType = KeyboardType.Ascii
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Row {
            Input(
                modifier = Modifier.fillMaxWidth(fraction = 0.65f),
                placeholder = "Аксиома",
                value = axiom,
                keyboardType = KeyboardType.Ascii
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Input(
                placeholder = "Угол",
                value = angle,
                keyboardType = KeyboardType.Number
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Цвета",
                style = TextStyle(
                    color = FractalTheme.WidgetText,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 16.sp
                ))
            Checkbox(
                checked = useColors.value,
                onCheckedChange = {
                    useColors.value = !useColors.value
                    vm.useColors.value = useColors.value
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = FractalTheme.Controllers,
                    uncheckedColor = FractalTheme.Controllers
                )
            )
        }

        AnimatedVisibility(visible = useColors.value) {
            Column {
                for (el in listOf(
                    Triple("+R", redChannel, Color(0xFFE25151)),
                    Triple("+G", greenChannel, Color(0xFF51E268)),
                    Triple("+B", blueChannel, Color(0xFF5168E2))
                )) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = el.first, style = TextStyle(
                            color = FractalTheme.WidgetText,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontSize = 16.sp
                        ))
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(text = "${el.second.value.toInt()}", style = TextStyle(
                            color = FractalTheme.HintText,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontSize = 16.sp
                        ))
                        Spacer(modifier = Modifier.padding(10.dp))
                        Slider(
                            value = el.second.value,
                            onValueChange = {
                                el.second.value = it
                            },
                            steps = 128,
                            valueRange = 0f..255f,
                            colors = SliderDefaults.colors(
                                thumbColor = el.third,
                                activeTrackColor = FractalTheme.SliderActive,
                                inactiveTrackColor = FractalTheme.SliderInactive
                            )
                        )
                    }

                }
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Input(
            placeholder = "Генерации",
            value = gens,
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Input(
            placeholder = "Шаг",
            value = step,
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.padding(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = FractalTheme.Controllers
            ),
            onClick = {
                vm.fractalLoading.value = true
                scope.launch {

                    state!!.bottomSheetState.partialExpand()
                    withContext(Dispatchers.Default) {
                        resCodes.value = vm.buildFractal(
                            vm.collectRules()
                        )
                    }

                    val resList = mutableListOf<String>()

                    Log.d("ROBOT", "[${vm.fractalImage.value?.width}, ${vm.fractalImage.value?.height}]")

                    if (resCodes.value and FractalResult.BORDER_OVERFLOW > 0)
                        resList.add("Изображение слишком велико и было обрезано")
                    if (resCodes.value and FractalResult.SYNTAX_ERROR > 0)
                        resList.add("Синтаксическая ошибка в формуле")
                    if (resCodes.value and FractalResult.EMPTY_SIDE > 0)
                        resList.add("Размерность изображения равна нулю")
                    if (resCodes.value and FractalResult.ROAD_OVERFLOW > 0)
                        resList.add("Конечная формула была сокращена")

                    val resText = resList.joinToString("\n")

                    if (resText.isNotEmpty())
                        Toast.makeText(context, resText, Toast.LENGTH_SHORT).show()

                    vm.fractalLoading.value = false
                }
            }
        ) {
            Text(
                text = "Построить",
                modifier = Modifier.padding(vertical = 5.dp),
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                fontSize = FractalTheme.TextButtonSize)
        }

        Spacer(modifier = Modifier.padding(20.dp))

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsPanelPreview() {
    val ctx = LocalContext.current
    SettingsPanel(
        vm = FractalViewModel((ctx.applicationContext as App).repository),
        state = null
    )
}





@Composable
fun fractalPlaceholder(txt: String): @Composable () -> Unit =
    @Composable {
        Text(
            text = txt,
            style = TextStyle(
                color = FractalTheme.HintText,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 16.sp
            ))
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    value: MutableState<String>,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {

    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value.value,
        onValueChange = { txt ->
            value.value = txt
        },
        modifier = modifier,
        visualTransformation = VisualTransformation.None,
        interactionSource = interactionSource,
        enabled = true,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        textStyle = TextStyle(
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 20.sp
        ),
        cursorBrush = SolidColor(Color.White)
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = value.value,
            shape = RoundedCornerShape(20.dp),
            placeholder = fractalPlaceholder(placeholder),
            visualTransformation = VisualTransformation.None,
            innerTextField = innerTextField,
            singleLine = true,
            enabled = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF888888),
                unfocusedContainerColor = Color(0xFF757575),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
            interactionSource = interactionSource,
            contentPadding = PaddingValues(15.dp, 10.dp), // this is how you can remove the padding
        )
    }
}

