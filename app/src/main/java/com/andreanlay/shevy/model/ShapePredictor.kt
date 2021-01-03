package com.andreanlay.shevy.model

import android.os.Build
import com.andreanlay.shevy.common.MINIMUM_CONFIDENCE_RATE
import com.andreanlay.shevy.common.SHAPES
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.nnapi.NnApiDelegate
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer

class ShapePredictor(model: MappedByteBuffer){
    private var interpreter: Interpreter? = null

    init {
        val options = Interpreter.Options()
        val nnApiDelegate: NnApiDelegate

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            nnApiDelegate = NnApiDelegate()
            options.addDelegate(nnApiDelegate)
        }

        interpreter = Interpreter(model, options)
    }

    fun solve(buffer: ByteBuffer): String {
        val output = Array(1) {
            FloatArray(SHAPES.size)
        }

        interpreter?.run(buffer, output)

        val result = output[0]
        val max = result.indices.maxByOrNull { result[it] }
        val confidenceRate = result[max!!] * 100
        val shape = SHAPES[max]

        val prediction = if(confidenceRate <= MINIMUM_CONFIDENCE_RATE) {
            "Sorry, I can't understand what you are drawing"
        } else {
            "I'm $confidenceRate% sure it's a $shape"
        }


        return prediction
    }
}