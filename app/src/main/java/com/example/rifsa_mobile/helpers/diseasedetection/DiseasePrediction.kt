package com.example.rifsa_mobile.helpers.diseasedetection

import android.content.Context
import android.graphics.Bitmap
import com.example.rifsa_mobile.ml.ModelQuantized
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DiseasePrediction(val context : Context) {

    private val executor : ExecutorService = Executors.newCachedThreadPool()

    fun classfication(bitmap: Bitmap): PredictionResult {
        val resizeImage = Bitmap.createScaledBitmap(
            bitmap,
            224,
            224,
            true
        )

        val imageBuffer = TensorImage.fromBitmap(resizeImage)

        val model = ModelQuantized.newInstance(context)
        val modelFeature = TensorBuffer.createFixedSize(
            intArrayOf(1, 224, 224, 3), DataType.UINT8
        )

        modelFeature.loadBuffer(imageBuffer.buffer)

        val outputPrediction = model.process(modelFeature)
        val resultPrediction = outputPrediction.outputFeature0AsTensorBuffer

        val labelPrediction = context.applicationContext.assets
            .open("labels-disease.txt")
            .bufferedReader()
            .use { it.readText() }

        val labelOutput = labelPrediction.split("\n")
        val maxArray = getMaxIndex(resultPrediction.intArray)

        model.close()
        return PredictionResult(
            maxArray,
            labelOutput[maxArray]
        )
    }

    fun initPrediction(bitmap: Bitmap): Task<PredictionResult>{
        val mainTask = TaskCompletionSource<PredictionResult>()
        executor.execute {
            val result = classfication(bitmap)
            mainTask.setResult(result)
        }
        return mainTask.task
    }

    private fun getMaxIndex(array : IntArray?): Int{
        if (array == null || array.isEmpty()) return -1
        var large = 0
        for (i in 1 until array.size){
            if (array[i] > array[large]) large = i
        }
        return large
    }


}