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

class MyChartLine(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val bounds = Rect()

    // Відступ для тексту днів тижня
    private val dayTextMargin = 80f  // Менший відступ для тексту днів тижня

    // Стиль для стовпців
    private val paintColumn = Paint().apply {
        color = Color.parseColor("#162437")  // Колір стовпців
    }

    // Стиль для тексту днів
    private val paintDayText = Paint().apply {
        color = Color.WHITE
        textSize = 35f
        textAlign = Paint.Align.RIGHT // Текст буде праворуч від краю, щоб не заходити на графік
        typeface = ResourcesCompat.getFont(context, R.font.mainfont)
    }

    // Стиль для чисел на стовпцях
    private val paintValueText = Paint().apply {
        color = Color.WHITE
        textSize = 25f
        textAlign = Paint.Align.LEFT
        typeface = ResourcesCompat.getFont(context, R.font.mainfont)
    }

    var barValues: FloatArray = floatArrayOf(10f, 15f, 8f, 10f, 20f, 6f, 10f)
        set(value) {
            field = value
            invalidate()  // Оновлюємо вигляд після зміни даних
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val barHeight = height / 7f  // Висота кожного стовпця
        val maxValue = barValues.maxOrNull() ?: 1f  // Максимальне значення для масштабування
        val maxBarWidth = width - dayTextMargin - 50f // Максимальна ширина стовпця (враховуючи відступ для днів тижня)

        // Рахуємо масштаб для стовпців, щоб вони не виходили за рамки
        val scale = maxBarWidth / maxValue

        val days = arrayOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Нд")

        for (i in barValues.indices) {
            val top = i * barHeight + 15f
            val right = (barValues[i] * scale) + dayTextMargin // Стовпці починаються після відступу для днів тижня
            val bottom = top + barHeight - 15f
            val left = dayTextMargin  // Всі стовпці починаються після відступу

            // Малюємо стовпець
            canvas.drawRect(left, top, right, bottom, paintColumn)

            // Малюємо назву дня тижня
            val day = days[i]
            paintDayText.getTextBounds(day, 0, day.length, bounds)
            val xTextDay = dayTextMargin - 10f  // Текст днів тижня розміщуємо дуже близько до лівого краю
            val yTextDay = top + (barHeight / 2 + bounds.height() / 2)
            canvas.drawText(day, xTextDay, yTextDay, paintDayText)

            // Малюємо значення стовпця
            val valueText = barValues[i].toInt().toString()
            paintValueText.getTextBounds(valueText, 0, valueText.length, bounds)
            val xTextValue = right + 20f  // Розміщуємо число праворуч від стовпця
            val yTextValue = top + (barHeight / 2 + bounds.height() / 2)
            canvas.drawText(valueText, xTextValue, yTextValue, paintValueText)
        }
    }
}
