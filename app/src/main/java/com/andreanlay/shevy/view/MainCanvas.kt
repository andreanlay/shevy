package com.andreanlay.shevy.view

import android.content.Context;
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View;
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.andreanlay.shevy.R
import com.andreanlay.shevy.common.OnCanvasCreateListener
import com.andreanlay.shevy.common.OnCanvasUpdateListener
import kotlin.math.abs

/*
    This is a custom view used to handle drawing from the app UI.
    it will override onSizeChanged, onDraw, and onTouch events from View

    Reference:
    Advance Android in Kotlin 02.2
    https://developer.android.com/codelabs/advanced-android-kotlin-training-canvas
 */

class MainCanvas(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var canvas: Canvas = Canvas()
    private lateinit var bitmap: Bitmap
    private val BACKGROUND_COLOR = ResourcesCompat.getColor(resources, R.color.black, null);
    private val DRAW_COLOR = ResourcesCompat.getColor(resources, R.color.white, null)
    private val STROKE_WIDTH = 7.5f

    private val MARGIN = 50

    private var touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private var touchX = 0f
    private var touchY = 0f
    private var currentX = 0f
    private var currentY = 0f

    private var canvasWidth: Int = -1
    private var canvasHeight: Int = -1

    private var path =  Path()
    private val paint = Paint().apply {
        color = DRAW_COLOR
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    private val rectPaint = Paint().apply {
        color = DRAW_COLOR
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
        alpha = 75
    }

    /*
        We create a custom event listener for Canvas because by default,
        canvas is supported by the data binding framework so there aren't
        listeners to allow the data binding framework to know when something
        has changed.

        So we need to create a custom listeners to let us know when
        the bound data is changed
     */
    private var onCanvasUpdateListener: OnCanvasUpdateListener? = null
    private var onCanvasCreateListener: OnCanvasCreateListener? = null

    fun setOnCanvasUpdateListener(listener: OnCanvasUpdateListener?) {
        this.onCanvasUpdateListener = listener
    }

    fun setOnCanvasCreateListener(listener: OnCanvasCreateListener?) {
        this.onCanvasCreateListener = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.canvasWidth = w
        this.canvasHeight = h

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        canvas.drawColor(BACKGROUND_COLOR)
        drawBorder()
        onCanvasCreateListener?.onCanvasCreated(bitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        onCanvasUpdateListener?.onCanvasUpdated(bitmap)
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
        updateView()
    }

    private fun touchStop() {
        path.reset()
    }

    private fun updateView() {
        invalidate()
    }

    private fun drawBorder() {
        canvas.drawRect(Rect(MARGIN, MARGIN, canvasWidth - MARGIN, canvasHeight - MARGIN - 700), rectPaint)
    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        drawBorder()
        updateView()
    }
}