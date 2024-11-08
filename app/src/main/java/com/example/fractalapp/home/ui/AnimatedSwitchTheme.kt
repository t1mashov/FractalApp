package com.example.fractalapp.home.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fractalapp.R
import com.example.fractalapp.ui.theme.FractalTheme

@Composable
fun AnimatedSwitchTheme(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isImageOneVisible by remember { mutableStateOf(FractalTheme.isDark) }

    val dayToNight = remember {
        context.getDrawable(R.drawable.theme_switch_day_night) as? AnimatedVectorDrawable
    }
    dayToNight?.setTint(FractalTheme.SwitchTheme.toArgb())

    val nightToDay = remember {
        context.getDrawable(R.drawable.theme_switch_night_day) as? AnimatedVectorDrawable
    }
    nightToDay?.setTint(FractalTheme.SwitchTheme.toArgb())

    val canChangeTheme = remember { mutableStateOf(false) }
    if (canChangeTheme.value) {
        FractalTheme.changeTheme()
        canChangeTheme.value = false
    }

    val currentImageRes =
        if (isImageOneVisible) R.drawable.theme_switch_day_night
        else R.drawable.theme_switch_night_day
    val currentAnim =
        if (isImageOneVisible) dayToNight
        else nightToDay

    AndroidView(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            isImageOneVisible = !isImageOneVisible
            canChangeTheme.value = true
            currentAnim?.start()
        },
        factory = {
            ImageView(it).apply {
                setImageResource(currentImageRes)
                setImageDrawable(currentAnim)
                currentAnim?.start()
            }
        },
        update = {
            it.setImageResource(currentImageRes)
            it.setImageDrawable(currentAnim)
            currentAnim?.start()
        }
    )
}