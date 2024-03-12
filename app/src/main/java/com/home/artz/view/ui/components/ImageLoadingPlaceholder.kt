package com.home.artz.view.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import com.home.artz.view.ui.theme.GreenSecondary

class ImageLoadingPlaceholder(private val width: Float, private val height: Float) : Painter() {
    override val intrinsicSize: Size
        get() = Size(
            width = width,
            height = height
        )

    override fun DrawScope.onDraw() {
        drawRect(GreenSecondary)
    }
}