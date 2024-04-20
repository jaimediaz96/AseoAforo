package com.example.aseoaforos.util

import android.app.Activity
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoUtil(private val activity: Activity) {
    var currentPhotoPath = ""

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun getPhotoUri(): Uri? {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            currentPhotoPath = ""
            return null
        }
        return FileProvider.getUriForFile(
            activity,
            "${activity.applicationContext.packageName}.provider",
            photoFile!!
        )
    }
}
