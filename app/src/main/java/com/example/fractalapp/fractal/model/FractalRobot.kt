package com.example.fractalapp.fractal.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import com.example.fractalapp.ui.theme.FractalTheme
import java.util.Stack
import kotlin.math.cos
import kotlin.math.sin

class FractalRobot : FractalBuilder {
    private data class RobotState(var x:Double, var y:Double, var rot:Double)
    private data class Pos(var x: Double, var y: Double)
    private data class Border(var min: Pos, var max: Pos)
    private data class Rule(val name:String, val value:String)

    private var rState = RobotState(0.0, 0.0, 0.0)
    private var result = FractalResult.SUCCESS


    override fun getResultCode(): Int = result

    override fun buildFractal(fr: FractalRules): Bitmap {
        result = FractalResult.SUCCESS

        fr.axiom = fr.axiom.replace(" ", "").lowercase()
        val road = prepareRules(fr)
        val borders = calculateImgSize(road, fr)
        initRobot()

        rState.x = -borders.min.x+1
        rState.y = -borders.min.y+1

        val size = mutableListOf(borders.max.x-borders.min.x, borders.max.y-borders.min.y)

        val maxSize = 5000.0
        if (size[0] > maxSize) {
            result = result or FractalResult.BORDER_OVERFLOW
            size[0] = maxSize
        }
        if (size[1] > maxSize) {
            result = result or FractalResult.BORDER_OVERFLOW
            size[1] = maxSize
        }
        if (size[0] == 0.0 || size[1] == 0.0) result = result or FractalResult.EMPTY_SIDE

        val bitmap = Bitmap.createBitmap(size[0].toInt()+5, size[1].toInt()+5, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            color = if (fr.useColors) Color.BLACK else FractalTheme.FractalColor.toArgb()
            strokeWidth = 1f
        }

        val alpha = 0xff000000.toInt()
        val canals = mutableListOf(0x0, 0x0, 0x0)

        executeRobot(
            road,
            fr,
            onForward = {from, to ->
                canvas.drawLine(from.x.toFloat(), from.y.toFloat(), to.x.toFloat(), to.y.toFloat(), paint)

                if (fr.useColors) {

                    paint.color = alpha or (canals[0] shl 16) or (canals[1] shl 8) or canals[2]

                    canals[0] = (canals[0] + fr.redChannel) % 0xff
                    canals[1] = (canals[1] + fr.greenChannel) % 0xff
                    canals[2] = (canals[2] + fr.blueChannel) % 0xff

                }

            }
        )

        Log.d("ROBOT", "size = [${bitmap.width}, ${bitmap.height}]")

        return bitmap
    }

    private fun initRobot() {
        rState.x = 0.0
        rState.y = 0.0
        rState.rot = Math.toRadians(-90.0)
    }
    private fun calculateImgSize(road: String, fr: FractalRules): Border {
        val minPos = Pos(0.0, 0.0)
        val maxPos = Pos(0.0, 0.0)

        initRobot()

        val findBorder: (Pos, Pos)->Unit = {_, to ->
            minPos.x = minOf(minPos.x, to.x)
            minPos.y = minOf(minPos.y, to.y)
            maxPos.x = maxOf(maxPos.x, to.x)
            maxPos.y = maxOf(maxPos.y, to.y)
        }
        executeRobot(
            road = road,
            fr = fr,
            onForward = findBorder,
            onJump = findBorder,
        )

        return Border(minPos, maxPos)
    }

    private fun executeRobot(
        road: String,
        fr: FractalRules,
        onForward: (Pos, Pos)->Unit = {from, to -> },
        onJump: (Pos, Pos)->Unit = {from, to -> },
    ) {

        val savedStack = Stack<RobotState>()

        for (c in road) {
            when (c) {
                'f', 'g' -> {
                    val x = (rState.x + fr.step * cos(rState.rot))
                    val y = (rState.y + fr.step * sin(rState.rot))
                    onForward(Pos(rState.x, rState.y), Pos(x, y))
                    rState.x = x
                    rState.y = y
                }
                'b' -> {
                    val x = (rState.x + fr.step * cos(rState.rot))
                    val y = (rState.y + fr.step * sin(rState.rot))
                    onJump(Pos(rState.x, rState.y), Pos(x, y))
                    rState.x = x
                    rState.y = y
                }
                '+' -> { rState.rot += Math.toRadians(fr.angle) }
                '-' -> { rState.rot -= Math.toRadians(fr.angle) }
                '(' -> {
                    savedStack.push(rState.copy())
                }
                ')' -> {
                    rState = savedStack.pop()
                }
            }
        }
    }

    private fun prepareRules(fr: FractalRules): String {
        var rules = listOf<Rule>()
        try {
            rules = fr.rules.replace(" ", "")
                .lowercase()
                .split(",")
                .map { it.split(":") }
                .filter { !(it[0].isEmpty() || it[1].isEmpty()) }
                .map { Rule(it[0], it[1]) }
        } catch (_: Exception) {
            result = result or FractalResult.SYNTAX_ERROR
        }

        var res = fr.axiom

        for (i in 0..<fr.gens) {

            var temp = ""
            for (c in res) {
                var tst = true
                for (rule in rules) {
                    if (rule.name == "$c") {
                        temp += rule.value
                        tst = false
                    }
                }
                if (tst) temp += c
            }

            res = temp

            // защита от перполнения памяти
            if (res.length > 30000) {
                result = result or FractalResult.ROAD_OVERFLOW
                return res
            }
        }

        return res
    }

}