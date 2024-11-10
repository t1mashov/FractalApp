package com.example.fractalapp.tutorial.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fractalapp.R

data class TutorialPage(
    val id: Int,
    val img: Int
)

@Composable
fun TutorialScreen(
    modifier: Modifier = Modifier
) {

    val pages = listOf(
        R.drawable.bg,
        R.drawable.bglight
    ).mapIndexed { index, el -> TutorialPage(index, el) }

    val pagerState = rememberPagerState { 2 }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            key = {pages[it].id}
        ) {pageIdx ->
            Image(
                painterResource(pages[pageIdx].img),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize())
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                repeat(pages.size) { index ->
                    val color by animateColorAsState(
                        targetValue = if (pagerState.currentPage == index) Color.Green else Color.Red
                    )
                    val size by animateDpAsState(
                        targetValue = if (pagerState.currentPage == index) 15.dp else 12.dp
                    )

                    Box(
                        modifier = Modifier
                            .size(size) // Размер точки
                            .padding(4.dp) // Расстояние между точками
                            .background(color, shape = CircleShape)
                    )
                }
            }
        }
    }



}

@Preview
@Composable
fun TutorialScreenPreview() {
    TutorialScreen()
}