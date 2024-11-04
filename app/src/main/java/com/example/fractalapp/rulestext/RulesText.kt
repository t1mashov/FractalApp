package com.example.fractalapp.rulestext

import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fractalapp.R
import com.example.fractalapp.ui.theme.FractalTheme

@Preview
@Composable
fun RulesText() {

    val vScroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(id = FractalTheme.Bg),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(vScroll)
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Правила L-Систем",
                color = FractalTheme.WidgetText,
                fontSize = FractalTheme.TextTitleSize,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            )

            Spacer(modifier = Modifier.height(30.dp))

            HtmlTextView(
                htmlContent = stringResource(id = R.string.l_system_text)
                    .replace("RulesTextColor", FractalTheme.RulesText.toRGBA())
                    .replace("RulesSelectionColor", FractalTheme.RulesSelection.toRGBA())
            )

        }

    }
}

fun Color.toRGBA(): String {
    val argb = this.toArgb().toLong()

    val r = (this.red * 255).toInt()
    val g = (this.green * 255).toInt()
    val b = (this.blue * 255).toInt()
    val a = (this.alpha * 255).toInt()

    return "%02X%02X%02X%02X".format(r, g, b, a)
}

@Composable
fun HtmlTextView(htmlContent: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true

                setBackgroundColor(android.graphics.Color.TRANSPARENT)

                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    )
}