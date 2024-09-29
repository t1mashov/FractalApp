package com.example.fractalapp.saves.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fractalapp.R
import com.example.fractalapp.fractal.ui.Input
import com.example.fractalapp.home.ui.FractalRow
import com.example.fractalapp.saves.SavesViewModel
import com.example.fractalapp.ui.theme.*

@Composable
fun SavesScreen(
    vm: SavesViewModel,
    navHostController: NavHostController
) {

    val fractals by
        vm.fractalListViewModel.fractals.observeAsState(initial = emptyList())

    val selection = remember { vm.fractalListViewModel.isSelectionExists() }
    
    val selectedFractalName = remember { vm.fractalListViewModel.selectedFractalName }

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.drawable.bg),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (fractals.isEmpty()) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Нет сохраненных фракталов",
                style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.montserrat_regular)
                    ),
                    fontSize = 20.sp,
                    color = WidgetText
                )
            )
        }
        else {

            AnimatedVisibility(visible = vm.fractalListViewModel.isSelected.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(color = BottomPanel)
                        .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                indication = rememberRipple(color = Color.White),
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                vm.deleteSelectedFromLiked()
                            },
                        imageVector = ImageVector.vectorResource(R.drawable.trash),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Controllers)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Input(
                        modifier = Modifier.weight(1f),
                        value = selectedFractalName, 
                        placeholder = "Название"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                vm.updateSelectedLiked()
                            },
                        imageVector = ImageVector.vectorResource(R.drawable.ok),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Controllers)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 30.dp)
            ) {

                vm.fractalListViewModel.getFractalList()?.let { fractals ->

//                    LazyVerticalGrid(
//                        modifier = Modifier
//                            .padding(start=25.dp, end=25.dp, top=25.dp),
//                        columns = GridCells.Fixed(2),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        items(
//                            items = it,
//                            key = { item -> item.id }
//                        ) {
//                            FractalWidget(
//                                it,
//                                navHostController,
//                                vm.fractalListViewModel,
//                                modifier = Modifier
//                                    .padding(horizontal = 5.dp, vertical = 5.dp)
//                                    .height(intrinsicSize = IntrinsicSize.Max)
//                            )
//                        }
//                    }

                    LazyColumn {
                        item {
                            Column {
                                Spacer(modifier = Modifier.height(30.dp))
                                Text(text = "Избранное",
                                    color = WidgetText,
                                    fontSize = TextTitleSize,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)))
                                Spacer(modifier = Modifier.padding(5.dp))
                            }
                        }
                        itemsIndexed(fractals) {idx, item ->
                            if (idx % 2 == 0)
                                FractalRow(
                                    first = item,
                                    second = if (idx+1 < fractals.size) fractals[idx+1] else null,
                                    vm = vm.fractalListViewModel,
                                    navController = navHostController
                                )
                        }
                    }

                }


                if (selection.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = null,
                                indication = null,
                            ) {
                                vm.fractalListViewModel.clearSelection()
                            }
                    )
                }

            }
        }

        if (vm.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    color = Controllers,
                    strokeWidth = 5.dp
                )
            }
        }

    }



}