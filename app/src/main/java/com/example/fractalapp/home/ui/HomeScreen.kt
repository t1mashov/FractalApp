package com.example.fractalapp.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fractalapp.App
import com.example.fractalapp.R
import com.example.fractalapp.home.FractalListWidgetViewModel
import com.example.fractalapp.home.HomeViewModel
import com.example.fractalapp.ui.theme.Controllers
import com.example.fractalapp.ui.theme.TextTitleSize
import com.example.fractalapp.ui.theme.WidgetText


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    navController: NavHostController?,
    vm: HomeViewModel,
) {

    val scroll = rememberScrollState()
    val isLoading = remember { vm.isLoading }

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.drawable.bg),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .padding(
                start = 30.dp,
                end = 30.dp
            )
            .verticalScroll(scroll)
    ) {
        Column(
            modifier = Modifier.padding(top = 30.dp)
        ) {
            Text(text = "Основное",
                color = WidgetText,
                fontSize = TextTitleSize,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)))
            Spacer(modifier = Modifier.padding(5.dp))
            MenuWidgetWithDescription(
                text = "Создать фрактал",
                description = "Фракталы L-системы, основаны на рекурсивных правилах",
                imgResource = R.drawable.play,
                firstColor = Color(0xFF2B916E),
                secondColor = Color(0xFF1D2B53),
                imgTint = Color(0xFF2DA57C),
                onClick = {
                    vm.redirectToFractalBuilder(navController!!)
                }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            MenuWidgetWithDescription(
                text = "Избранное",
                description = "Сохраняйте данные понравившихся фракталов\n",
                imgResource = R.drawable.star,
                firstColor = Color(0xFFA2762C),
                secondColor = Color(0xFF692DA5),
                imgTint = Color(0xFFAD8034),
                onClick = {
                    vm.redirectToSaves(navController!!)
                }
            )

            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                text = "Информация",
                color = WidgetText,
                fontSize = TextTitleSize,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Row(
                modifier = Modifier
                    .height(intrinsicSize = IntrinsicSize.Max)
            ) {
                InfoWidget(
                    modifier = Modifier.weight(1f),
                    text = "Правила L-системы",
                    description = "Подробный разбор структуры L-систем с примерами",
                    color = Color(0xFF226B7A)
                )

                Spacer(modifier = Modifier.padding(5.dp))

                InfoWidget(
                    modifier = Modifier.weight(1f),
                    text = "Обучение\n",
                    description = "Гайд по приложению и особенностям формул фракталов",
                    color = Color(0xFF8B4E65)
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                text = "Примеры фракталов",
                color = WidgetText,
                fontSize = TextTitleSize,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            )
            Spacer(modifier = Modifier.padding(5.dp))

            if (isLoading.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = Controllers,
                        strokeWidth = 5.dp
                    )
                }
            }
        }

        FractalListWidget(
            vm = vm.fractalListViewModel,
            navController = navController,
        )
    }


}


@Preview
@Composable
fun HomeScreenPreview() {

    val ctx = LocalContext.current

//    HomeScreen(
//        navController = null,
//        vm = HomeViewModel((ctx.applicationContext as App).repository)
//    )
}