package com.example.rifsa_mobile.utils

import android.app.Application
import android.util.Patterns
import com.example.rifsa_mobile.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    //give file name format
    private const val FILENAME_FORMAT = "dd-MMM-yyyy"
    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())


    fun CharSequence.validEmailChecker() =
        Patterns
            .EMAIL_ADDRESS.matcher(this)
            .matches()


    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }
}