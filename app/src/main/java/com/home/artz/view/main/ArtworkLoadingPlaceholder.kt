package com.home.artz.view.main

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.util.toRange
import com.home.artz.view.ui.theme.GreenSecondary

class ArtworkLoadingPlaceholder(private val screenWidth: Int, private val screenHeight: Int) : Painter() {
    override val intrinsicSize: Size
        get() = Size(
            width = screenWidth * 0.4F,
            height = screenHeight * 0.3F
        )

    override fun DrawScope.onDraw() {
        drawRect(GreenSecondary)
    }
}