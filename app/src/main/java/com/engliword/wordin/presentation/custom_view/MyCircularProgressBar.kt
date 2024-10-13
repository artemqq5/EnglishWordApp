package com.engliword.wordin.presentation.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.engliword.wordin.R
import kotlin.math.min

class MyCircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val bounds = Rect()

    private val circlePaint = Paint().apply {
        color = Color.parseColor("#162437")
        style = Paint.Style.STROKE
        strokeWidth = 25f
    }

    private val progressPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        typeface = ResourcesCompat.getFont(context, R.font.mainfont)
        textSize = 35f
        textAlign = Paint.Align.CENTER
    }

    private val practiceText = Paint().apply {
        typeface = ResourcesCompat.getFont(context, R.font.mainfont)
        color = Color.WHITE
        textSize = 35f
        textAlign = Paint.Align.CENTER
    }

    var progress: Float = 35f
        set(value) {
            field = value
            invalidate()
        }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val barWidth = width.toFloat()
        val barHeight = height.toFloat()

        val text = "Практика"
        practiceText.getTextBounds(text, 0, text.length, bounds)

        val centerX = width / 2f
        val centerY = height / 2f - bounds.height()
        val radius = (barHeight - bounds.height() - 120f) / 2f

        // Коло
        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        // Коло %
        val sweepAngle = (progress / 100) * 360
        canvas.drawArc(
            centerX - radius, centerY - radius,
            centerX + radius, centerY + radius,
            -90f, sweepAngle, false, progressPaint
        )

        // Малюємо текст
        val xText = barWidth/2
        val yText = height - bounds.height().toFloat()
        canvas.drawText(text, xText, yText, practiceText)

        // Малюємо текст прогресу всередині кола
        val progressText = "$progress%"
        canvas.drawText(progressText, centerX, centerY + textPaint.textSize / 3, textPaint)




    }

//    private val bounds = Rect()
//

//
//
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        val width = width.toFloat()
//        val height = height.toFloat()
//
//        // Відступи для кола і тексту
////        val paddingHorizontal = 15f // Відступ зліва і справа
////        val paddingVertical = 100f // Відступ зверху та знизу
//
//        // Доступна ширина для кола
//        val availableWidth = width // - paddingHorizontal * 2
//        // Доступна висота для кола (враховуємо місце для тексту знизу)
//        val availableHeight = height // - textPaint.textSize - paddingVertical
//
//        // Визначаємо радіус кола і позицію центру
////        val radius = height/2 - 50f // - circlePaint.strokeWidth
////        val centerX = width / 2
////        val centerY = height / 2 // Центруємо коло вертикально
//
//        // Малюємо коло
////        canvas.drawCircle(centerX, centerY, radius, circlePaint)
//
//
//
//        // Малюємо текст "Практика" на рівні з підписами у графіку зліва
////        val practiceWord = "Практика"
////        practiceText.getTextBounds(practiceWord, 0, practiceWord.length, bounds)
////        val xText = width / 2f
//        // Позиціонуємо текст "Практика" так, щоб він розташовувався на рівні з іншими підписами
////        val yText = centerY + radius + bounds.height() + 20f // Відступ 20f від кола
////        canvas.drawText(practiceWord, xText, yText, practiceText)
//
//        // Малюємо дугу, яка представляє прогрес
////        val sweepAngle = (progress / 100) * 360
////        canvas.drawArc(
////            centerX - radius, centerY - radius,
////            centerX + radius, centerY + radius,
////            -90f, sweepAngle, false, progressPaint
////        )
//    }
}
