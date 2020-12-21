package com.andreanlay.shevy.view

import android.content.Context;
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View;
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.andreanlay.shevy.R
import kotlin.math.abs

class MainCanvas(context: Context, attrs: AttributeSet) : View(context) {
    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap
    private val BACKGROUND_COLOR = ResourcesCompat.getColor(resources, R.color.black, null);
    private val DRAW_COLOR = ResourcesCompat.getColor(resources, R.color.white, null)
    private val STROKE_WIDTH = 12f

    private var touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private var touchX = 0f
    private var touchY = 0f
    private var currentX = 0f
    private var currentY = 0f

    private var path = Path()
    private val paint = Paint().apply {
        color = DRAW_COLOR
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // To prevent memory leak caused by old bitmaps
        if (::bitmap.isInitialized) {
            bitmap.recycle()
        }

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        canvas.drawColor(BACKGROUND_COLOR)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchX = event.x
        touchY = event.y

        when(event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchStop()
        }

        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(touchX, touchY)

        currentX = touchX
        currentY = touchY
    }

    private fun touchMove() {
        val dx = abs(touchX - currentX)
        val dy = abs(touchY - currentY)

        if(dx >= touchTolerance && dy >= touchTolerance) {
            path.quadTo(currentX, currentY, (touchX + currentX) / 2, (touchY + currentY) / 2)
            currentX = touchX
            currentY = touchY

            canvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchStop() {
        path.reset()
    }
}