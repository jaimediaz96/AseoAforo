package com.example.aseoaforos.util

import android.app.Activity
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUtil(private val activity: Activity) {
    var currentPath = ""

    fun createImageFile(): File? {
        return try {
            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            File.createTempFile(
                "PNG_${timeStamp}_",
                ".png",
                storageDir
            ).apply {
                currentPath = absolutePath
            }
        } catch (e: Exception) {
            currentPath = ""
            null
        }
    }
}
