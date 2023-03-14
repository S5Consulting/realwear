package com.akso.realwear.app.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ProgressBar
import com.akso.realwear.R

class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.progressBarStyle,
    ) : ProgressBar(context,attrs,defStyleAttr) {

        private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 10f
        }

    private val textBounds: Rect = Rect()

    var progressText: String = ""
    set(value) {
        field = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        textPaint.getTextBounds(progressText, 0, progressText.length, textBounds)
        val x = (width / 2f) - (textBounds.width() / 2f)
        val y = (height / 2f) + (textBounds.height() / 2f)
        canvas.drawText(progressText, x, y, textPaint)


    }
}