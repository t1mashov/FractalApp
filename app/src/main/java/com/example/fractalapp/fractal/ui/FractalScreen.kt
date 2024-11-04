package com.example.fractalapp.fractal.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fractalapp.AppFractal
import com.example.fractalapp.R
import com.example.fractalapp.fractal.FractalViewModel
import com.example.fractalapp.ui.theme.FractalTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FractalScreen(
    vm: FractalViewModel,
    app: AppFractal?
) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val ctx = LocalContext.current

    val canLoad = remember { mutableStateOf(true)}
    if (canLoad.value) {
        LaunchedEffect(Unit) {
            vm.loadFractal()
            canLoad.value = false
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            SettingsPanel(vm, scaffoldState)
        },
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(color = Color.White)
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContainerColor = FractalTheme.BottomPanel
    ) {

        ZoomFractal(vm)

        if (vm.canShowDialog()) {
            SaveFractalDialog(
                onDismiss = { vm.disableDialog() },
                vm = vm,
                ctx = ctx
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp, horizontal = 30.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            color = FractalTheme.ScreenBackgroundTAlpha,
                            shape = RoundedCornerShape(FractalTheme.WidgetCorner)
                        )
                        .padding(2.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(5.dp)
                            .clickable {
                                vm.enableDialog()
                            },
                        imageVector = ImageVector.vectorResource(R.drawable.star),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(FractalTheme.Controllers))
                }

                Spacer(modifier = Modifier.padding(15.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = FractalTheme.ScreenBackgroundTAlpha,
                            shape = RoundedCornerShape(FractalTheme.WidgetCorner)
                        )
                        .padding(2.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(5.dp)
                            .clickable {
                                app!!.trySaveFractal(vm.fractalImage.value)
                            },
                        imageVector = ImageVector.vectorResource(R.drawable.download),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(FractalTheme.Controllers)
                    )
                }

                Spacer(modifier = Modifier.padding(15.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = FractalTheme.ScreenBackgroundTAlpha,
                            shape = RoundedCornerShape(FractalTheme.WidgetCorner)
                        )
                        .padding(2.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(5.dp)
                            .clickable {
                                scope.launch {
                                    when (scaffoldState.bottomSheetState.currentValue) {
                                        SheetValue.PartiallyExpanded -> scaffoldState.bottomSheetState.expand()
                                        SheetValue.Hidden -> scaffoldState.bottomSheetState.expand()
                                        SheetValue.Expanded -> scaffoldState.bottomSheetState.partialExpand()
                                    }
                                }
                            },
                        imageVector = ImageVector.vectorResource(R.drawable.fractal_params),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(FractalTheme.Controllers)
                    )
                }

            }
        }

    }
}


@Preview
@Composable
fun Img() {
    Box(
        modifier = Modifier
            .background(
                color = FractalTheme.BgColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(5.dp)
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .padding(5.dp)
                .clickable {},
            imageVector = ImageVector.vectorResource(R.drawable.star),
            contentDescription = null,
            colorFilter = ColorFilter.tint(FractalTheme.Controllers))
    }
}

