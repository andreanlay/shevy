package com.andreanlay.shevy.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.databinding.*
import androidx.databinding.library.baseAdapters.BR
import com.andreanlay.shevy.model.CanvasBitmap
import kotlin.properties.Delegates

class MainViewModel: BaseObservable() {
    private var canvasBitmap = CanvasBitmap()

    private lateinit var prediction: String
    private var confidenceRate by Delegates.notNull<Float>()

    /*
        This function handle onCanvasUpdated() events
        called from MainCanvas view
    */
    fun canvasUpdated(bitmap: Bitmap) {
        canvasBitmap.setBitmap(bitmap)
    }

    /*
        This function handle onCanvasCreated() events
        called from MainCanvas view
    */
    fun canvasCreated(bitmap: Bitmap) {
        canvasBitmap.setBitmap(bitmap)
    }

    fun onPredictClicked() {
        Log.d("ViewModel", "Predict!")
    }

    fun onClearClicked() {
        canvasBitmap.clear()
        notifyPropertyChanged(BR.canvasBitmap)
    }

    @Bindable
    fun getCanvasBitmap(): Bitmap {
        return canvasBitmap.getBitmap()
    }
}