package com.example.fractalapp.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fractalapp.App
import com.example.fractalapp.R
import com.example.fractalapp.home.FractalListWidgetViewModel
import com.example.fractalapp.home.HomeViewModel
import com.example.fractalapp.ui.theme.FractalTheme


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    navController: NavHostController?,
    vm: HomeViewModel,
) {

    val isLoading = remember { vm.isLoading }

    HomeScreenDisplay(
        isLoading = isLoading,
        redirectToSaves = {
            vm.redirectToSaves(navController!!)
        },
        redirectToFractalBuilder = {
            vm.redirectToFractalBuilder(navController!!)
        },
        redirectToRulesText = {
            vm.redirectToRulesText(navController!!)
        },
        fractalListWidget = {
            FractalListWidget(
                vm = vm.fractalListViewModel,
                navController = navController,
            )
        }
    )


}


@Preview
@Composable
fun HomeScreenPreview() {
//    FractalTheme.setDark()
    FractalTheme.setLight()
    HomeScreenDisplay()
}


@Composable
fun HomeScreenDisplay(
    isLoading: MutableState<Boolean> = mutableStateOf(false),
    redirectToFractalBuilder: () -> Unit = {},
    redirectToSaves: () -> Unit = {},
    redirectToRulesText: () -> Unit = {},
    fractalListWidget: @Composable () -> Unit = {}
) {

    val scroll = rememberScrollState()

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = FractalTheme.Bg),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier
            .padding(
                start = 30.dp,
                end = 30.dp
            )
            .verticalScroll(scroll)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            AnimatedSwitchTheme(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(40.dp)
            )
        }
        Column {
            Column(
                modifier = Modifier.padding(top = 30.dp)
            ) {
                Text(text = "Основное",
                    color = FractalTheme.WidgetText,
                    fontSize = FractalTheme.TextTitleSize,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)))
                Spacer(modifier = Modifier.padding(5.dp))
                MenuWidgetWithDescription(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 130.dp),
                    text = "Создать фрактал",
                    description = "Фракталы L-системы, основаны на рекурсивных правилах",
                    imgResource = R.drawable.play,
                    firstColor = FractalTheme.CreateFractalFirst,
                    secondColor = FractalTheme.CreateFractalSecond,
                    imgTint = FractalTheme.CreateFractalTint,
                    onClick = {
                        redirectToFractalBuilder()
                    }
                )
                Spacer(modifier = Modifier.padding(7.dp))
                MenuWidgetWithDescription(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 130.dp),
                    text = "Избранное",
                    description = "Сохраняйте данные понравившихся фракталов",
                    imgResource = R.drawable.star,
                    firstColor = FractalTheme.SavesFractalFirst,
                    secondColor = FractalTheme.SavesFractalSecond,
                    imgTint = FractalTheme.SavesFractalTint,
                    onClick = {
                        redirectToSaves()
                    }
                )

                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    text = "Информация",
                    color = FractalTheme.WidgetText,
                    fontSize = FractalTheme.TextTitleSize,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier
                        .height(intrinsicSize = IntrinsicSize.Max)
                ) {
                    InfoWidget(
                        modifier = Modifier
                            .weight(1f),
                        text = "Правила L-системы",
                        description = "Подробный разбор структуры L-систем с примерами",
                        color = FractalTheme.RulesLSystemBg,
                        onClick = {
                            redirectToRulesText()
                        }
                    )

                    Spacer(modifier = Modifier.padding(5.dp))

                    InfoWidget(
                        modifier = Modifier.weight(1f),
                        text = "Обучение\n",
                        description = "Гайд по приложению и особенностям формул фракталов",
                        color = FractalTheme.TutorialBg,
                        onClick = {

                        }
                    )
                }

                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    text = "Примеры фракталов",
                    color = FractalTheme.WidgetText,
                    fontSize = FractalTheme.TextTitleSize,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                )
                Spacer(modifier = Modifier.padding(5.dp))

                if (isLoading.value) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color = FractalTheme.Controllers,
                            strokeWidth = 5.dp
                        )
                    }
                }
            }

            fractalListWidget()
        }

    }

}