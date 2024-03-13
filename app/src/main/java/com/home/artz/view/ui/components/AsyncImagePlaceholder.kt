package com.home.artz.view.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import com.home.artz.view.ui.theme.GreenSecondary

class AsyncImagePlaceholder(private val widthDp: Float, private val heightDp: Float) : Painter() {
    override val intrinsicSize: Size
        get() = Size(
            width = widthDp,
            height = heightDp
        )

    override fun DrawScope.onDraw() {
        drawRect(GreenSecondary)
    }
}