package com.example.fractalapp.ui.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.fractalapp.R
import kotlinx.coroutines.launch

object FractalTheme {
    private var isDark by mutableStateOf(true)
    val themeObservers = hashMapOf<String, (Boolean) -> Unit>()

    @Composable
    fun updateStatusBar() {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                window.statusBarColor = ScreenBackground.toArgb()
                window.navigationBarColor = ScreenBackground.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDark
            }
        }

    }

    fun setLight() {
        isDark = false
    }
    fun setDark() {
        isDark = true
    }

    @Composable
    fun changeTheme() {
        isDark = !isDark
        updateStatusBar()

        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            scope.launch {
                for ((_, ob) in themeObservers) {
                    ob(isDark)
                }
            }
        }
    }

    val WidgetText
        get() = if (isDark) Color(0xFFDCDCDC) else Color(0xFF212121)
    val SubTitlesText
        get() = if (isDark) Color(0xB0D8D8D8) else Color(0xB0323232)
    val HintText
        get() = if (isDark) Color(0xFFD1D1D1) else Color(0xFF404040)
    val WidgetFractalSampleBackground
        get() = if (isDark) Color(0xDC464646) else Color(0xDCD4D4D4)
    val WidgetFractalSampleBackgroundLongPressed
        get() = if (isDark) Color(0xDC7E7E7E) else Color(0xDCAAAAAA)
    val SliderActive
        get() = if (isDark) Color(0xAE6D6D6D) else Color(0xAEA7A7A7)
    val SliderInactive
        get() = if (isDark) Color(0xAE333333) else Color(0xAED2D2D2)
    val ScreenBackground
        get() = if (isDark) Color(0xFF161616) else Color(0xFFEEEEEE)
    val ScreenBackgroundTAlpha
        get() = if (isDark) Color(0x95000000) else Color(0x95FAFAFA)
    val BottomPanel
        get() = if (isDark) Color(0xFF444444) else Color(0xFFD4D4D4)
    val Ripple
        get() = if (isDark) Color(0xFFFFFFFF) else Color(0xFF000000)
    val Controllers
        get() = if (isDark) Color(0xFFCE9D4A) else Color(0xFFC78D13)
    val ButtonText
        get() = if (isDark) Color(0xFF1A1A1A) else Color(0xFFE2E2E2)

    val CreateFractalFirst
        get() = if (isDark) Color(0xFF2B916E) else Color(0xFF5D88D1)
    val CreateFractalSecond
        get() = if (isDark) Color(0xFF1D2B53) else Color(0xFF95D15C)
    val CreateFractalTint
        get() = if (isDark) Color(0xFF2B916E) else Color(0xFF527ECA)

    val SavesFractalFirst
        get() = if (isDark) Color(0xFFA2762C) else Color(0xFFE0D1AA)
    val SavesFractalSecond
        get() = if (isDark) Color(0xFF692DA5) else Color(0xFFD47091)
    val SavesFractalTint
        get() = if (isDark) Color(0xFFAD8034) else Color(0xFFD36E90)

    val RulesLSystemBg
        get() = if (isDark) Color(0xFF226B7A) else Color(0xFF7DB096)
    val TutorialBg
        get() = if (isDark) Color(0xFF8B4E65) else Color(0xFFD99A9B)

    val RulesText
        get() = if (isDark) Color(0xBFFFFFFF) else Color(0xFF000000)
    val RulesSelection
        get() = if (isDark) Color(0xFF00FFD9) else Color(0xFF00717F)

    val FractalColor
        get() = if (isDark) Color(0xFFFFFFFF) else Color(0xFF01183B)

    val BgColor
        get() = if (isDark) Color(0xFF000000) else Color(0xFFFFFFFF)
    val Bg
        get() = if (isDark) R.drawable.bg else R.drawable.bglight

    val WidgetCorner = 10.dp

    val TextTitleSize = 20.sp
    val TextWidgetTitleSize = 22.sp
    val TextDescriptionSize = 13.sp
    val TextButtonSize = 16.sp

}