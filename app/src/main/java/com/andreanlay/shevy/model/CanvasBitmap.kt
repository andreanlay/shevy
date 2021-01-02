package com.andreanlay.shevy.model

import android.graphics.*
import com.andreanlay.shevy.common.CANVAS_MARGIN
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.properties.Delegates


class CanvasBitmap {
    private var bitmap: Bitmap
    private var width by Delegates.notNull<Int>()
    private var height by Delegates.notNull<Int>()

    init {
        bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }

    fun clear() {
        bitmap.eraseColor(Color.TRANSPARENT)
    }

    fun setBitmap(bitmap: Bitmap) {
        this.width = bitmap.width
        this.height = bitmap.height

        this.bitmap = bitmap
    }

    fun getBitmap(): Bitmap {
        return bitmap
    }

    private fun cropBitmap(bitmap: Bitmap): Bitmap {
        val rectWidth = 2 * CANVAS_MARGIN
        return Bitmap.createBitmap(bitmap, rectWidth + 1, rectWidth + 1,
                width - CANVAS_MARGIN - rectWidth - 8,
                (0.5 * height).toInt() - rectWidth - 8)
    }

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, 28, 28, true)
    }

    /*
        This function convert bitmap to processable buffer for the TF Lite model
        it also converts bitmap to grayscale by adding each channel (RGB) value then get its
        average and normalize it to [0..1] range

        Reference: Build a handwritten digit classifier app with TensorFlow Lite
        https://developer.android.com/codelabs/digit-classifier-tflite#0
     */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(Float.SIZE_BYTES * 28 * 28 * 1)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(28 * 28)
        bitmap.getPixels(pixels, 0, 28, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF)
            val g = (pixelValue shr 8 and 0xFF)
            val b = (pixelValue and 0xFF)

            val normalizedPixelValue = (r + g + b) / 3.0f / 255.0f
            byteBuffer.putFloat(normalizedPixelValue)
        }

        return byteBuffer
    }

    fun getImage(): ByteBuffer {
        val tempBitmap = bitmap.copy(bitmap.config, true)
        val croppedBitmap = cropBitmap(tempBitmap)
        val resizedBitmap = resizeBitmap(croppedBitmap)

        return convertBitmapToByteBuffer(resizedBitmap)
    }
}