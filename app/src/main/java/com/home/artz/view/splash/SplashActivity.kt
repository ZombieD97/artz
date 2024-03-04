package com.home.artz.view.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.home.artz.view.ui.theme.Accent
import com.home.artz.view.ui.theme.ArtzTheme
import com.home.artz.view.ui.theme.Tertiary

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContent {
            ArtzTheme {
                SplashScreen()
            }
        }
    }

    @Composable
    fun SplashScreen() {
        val mainColor = MaterialTheme.colorScheme.primary
        val config = LocalConfiguration.current

        val animateMove = remember { Animatable(0.5F) }
        LaunchedEffect(animateMove) {
            animateMove.animateTo(
                targetValue = 0F,
                animationSpec = tween(durationMillis = 10000, easing = LinearEasing)
            )
        }

        val animateTriangleWidth = remember { Animatable(1F) }
        LaunchedEffect(animateTriangleWidth) {
            animateTriangleWidth.animateTo(
                targetValue = 0F,
                animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
            )
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), onDraw = {
            val screenWidthPx = config.screenWidthDp.dp.toPx()
            val screenHeightPx = config.screenHeightDp.dp.toPx()
            val centerCircleSize = screenWidthPx.half()
            val centerCircleRadius = centerCircleSize.half()

            drawCircle(mainColor, radius = centerCircleRadius)

            val littleCircleSize = screenWidthPx * 0.07F
            val yAnimatedPosition = animateMove.value * screenHeightPx
            drawCircle(
                color = Accent,
                radius = littleCircleSize,
                center = Offset(
                    screenWidthPx.half(),
                    yAnimatedPosition
                )
            )

            val triangleSize = littleCircleSize * 3
            val triangleAnimatedWidth = if (yAnimatedPosition <= screenHeightPx.half() - centerCircleRadius) {
                // start animation here
                triangleSize * animateTriangleWidth.value
            } else {
                triangleSize
            }
            drawFilledTriangle(
                Tertiary,
                Offset(
                    screenWidthPx.half(),
                    yAnimatedPosition - littleCircleSize
                ),
                Size(triangleAnimatedWidth, triangleSize)
            )
        })
    }

    private fun DrawScope.drawFilledTriangle(color: Color, offset: Offset, size: Size) {
        val path = Path()
        path.moveTo(offset.x, offset.y)
        path.lineTo(offset.x + size.width.half(), offset.y + size.height)
        path.lineTo(offset.x - size.width.half(), offset.y + size.height)
        path.lineTo(offset.x, offset.y)
        path.fillType = PathFillType.EvenOdd
        drawPath(path, color)
    }

    private fun Float.half() = this * 0.5F
}