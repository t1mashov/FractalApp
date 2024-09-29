package com.example.fractalapp.fractal

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fractalapp.db.BitmapConverter
import com.example.fractalapp.db.Fractal
import com.example.fractalapp.db.FractalRepository
import com.example.fractalapp.fractal.model.FractalBuilder
import com.example.fractalapp.fractal.model.FractalRobot
import com.example.fractalapp.fractal.model.FractalRules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FractalViewModel(

    private val repository: FractalRepository,
    private val robot: FractalBuilder = FractalRobot(),

    val fractalImage: MutableState<Bitmap?> = mutableStateOf(
        Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    ),
    val fractalLoading: MutableState<Boolean> = mutableStateOf(false),
    val fractalScale: MutableState<Float> = mutableStateOf(1f),
    val fractalOffset: MutableState<Offset> = mutableStateOf(Offset(0f, 0f)),

    val rules: MutableState<String> = mutableStateOf(""),
    val axiom: MutableState<String> = mutableStateOf(""),
    val angle: MutableState<String> = mutableStateOf(""),
    val gens: MutableState<String> = mutableStateOf(""),
    val step: MutableState<String> = mutableStateOf(""),
    val useColors: MutableState<Boolean> = mutableStateOf(false),
    val resCodes: MutableState<Int> = mutableStateOf(0),
    val redChannel: MutableState<Float> = mutableStateOf(0f),
    val greenChannel: MutableState<Float> = mutableStateOf(0f),
    val blueChannel: MutableState<Float> = mutableStateOf(0f),

    var id: Int? = null,
    var sid: Int? = null,
    private val showDialog: MutableState<Boolean> = mutableStateOf(false),
    val fractalName: MutableState<String> = mutableStateOf("")

): ViewModel() {

    fun canShowDialog() = showDialog.value
    fun disableDialog() {
        showDialog.value = false
    }
    fun enableDialog() {
        fractalName.value = ""
        showDialog.value = true
    }

    fun loadFractal() {
        if ((id != null && id != -1) || (sid != null && sid != -1)) {
            viewModelScope.launch(Dispatchers.IO) {

                val fractal =
                    if (id != null && id != -1) repository.getSampleById(id!!)
                    else repository.getLikedById(sid!!)

                rules.value = fractal.rules
                axiom.value = fractal.axiom
                angle.value = fractal.angle
                gens.value = fractal.gens
                step.value = fractal.step
                useColors.value = fractal.useColors
                redChannel.value = fractal.redChannel.toFloat()
                greenChannel.value = fractal.greenChannel.toFloat()
                blueChannel.value = fractal.blueChannel.toFloat()

                fractalImage.value = BitmapConverter.toBitmap(fractal.icon)
            }
        }

    }

    fun collectRules(): FractalRules = FractalRules(
        rules = rules.value,
        axiom = axiom.value,
        angle = if (angle.value.isNotEmpty() && angle.value.isDigitsOnly())
            angle.value.toDouble() else 90.0,
        gens = if (gens.value.isNotEmpty() && gens.value.isDigitsOnly())
            gens.value.toInt() else 5,
        step = if (step.value.isNotEmpty() && step.value.isDigitsOnly())
            step.value.toInt() else 5,
        useColors = useColors.value,
        redChannel = redChannel.value.toInt(),
        greenChannel = greenChannel.value.toInt(),
        blueChannel = blueChannel.value.toInt()
    )

    fun buildFractal(
        rules: FractalRules
    ): Int {
        fractalImage.value = robot.buildFractal(
            rules
        )
        return robot.getResultCode()
    }

    fun clearSettings() {
        rules.value = ""
        axiom.value = ""
        angle.value = ""
        gens.value = ""
        step.value = ""
        useColors.value = false
        redChannel.value = 0f
        greenChannel.value = 0f
        blueChannel.value = 0f
    }

    fun saveFractal(ctx: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fractalImage.value?.let {
                    repository.addFractal(Fractal(
                        title = fractalName.value.ifEmpty { Fractal.defaultName() },
                        icon = (BitmapConverter.fromBitmap(it))!!,
                        rules = rules.value,
                        axiom = axiom.value,
                        angle = angle.value,
                        gens = gens.value,
                        step = step.value,
                        useColors = useColors.value,
                        redChannel = redChannel.value.toInt(),
                        greenChannel = greenChannel.value.toInt(),
                        blueChannel = blueChannel.value.toInt(),
                    ))
                }
            }

            Toast.makeText(ctx, "Фрактал сохранен в избранное", Toast.LENGTH_SHORT).show()
        }
    }

}
