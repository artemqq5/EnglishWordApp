package com.engliword.wordin.presentation.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.engliword.wordin.R

class MyChartLine2(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val bounds = Rect()

    private val paintColumn = Paint().apply {
        color = Color.parseColor("#162437")
    }
    private val paintDayText = Paint().apply {
        typeface = ResourcesCompat.getFont(context, R.font.mainfont)
        color = Color.WHITE
        textSize = 35f
        textAlign = Paint.Align.CENTER
    }

    private val paintCountText = Paint().apply {
        typeface = ResourcesCompat.getFont(context, R.font.mainfont)
        color = Color.WHITE
        textSize = 25f
        textAlign = Paint.Align.CENTER
    }
    var dayValues: FloatArray = floatArrayOf(345f, 67f)
        set(value) {
            field = value
            invalidate()
        }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val barWidth = width / 2f  // Вираховуємо ширину кожного стовпця
        val maxValue = dayValues.max()  // Максимальне значення стовпця
        val unitHeight = height / maxValue  // Вираховуємо висоту одиниці значення на графіку

        for (i in dayValues.indices) {
            val left = i * barWidth + 15f
            var topColumn = (height - (dayValues[i] * unitHeight))
            val right = left + barWidth - 15f
            val bottomColumn = height - 100f


            if (height - topColumn <= 150f && dayValues[i] != 0f) {
                topColumn = bottomColumn - 50f
            } else if (dayValues[i] == 0f) {
                topColumn = bottomColumn
            }



            val text = arrayOf("Слова", "Ідіоми")
            paintDayText.getTextBounds(text[i], 0, text[i].length, bounds)

            val xText = right-(right-left)/2
            val yText = height - bounds.height().toFloat()

            // Малюємо стовпець на графіку
            canvas.drawRect(left, topColumn, right, bottomColumn, paintColumn)

            // Малюємо текст
            canvas.drawText(text[i], xText, yText, paintDayText)

            paintCountText.getTextBounds("${dayValues[i]}", 0, "${dayValues[i]}".length, bounds)

            // Малюємо текст 2
            if (dayValues[i] != 0f) {
                canvas.drawText("${dayValues[i].toInt()}", xText, topColumn+35f, paintCountText)
            }

        }
    }

}