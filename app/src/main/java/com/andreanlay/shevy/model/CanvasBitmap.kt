package com.andreanlay.shevy.model

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
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
}