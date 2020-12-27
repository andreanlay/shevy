package com.andreanlay.shevy.common

import android.graphics.Bitmap

interface OnCanvasUpdateListener {
    fun onCanvasUpdated(bitmap: Bitmap)
}