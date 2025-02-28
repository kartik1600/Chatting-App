package com.example.chattingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.lifecycle.ViewModel
import java.io.ByteArrayOutputStream
import java.io.File

class MyViewModel : ViewModel() {

    fun checkEmpty(id: String, pass: String? = "Abcde@123", context: Context): Boolean {
        if (id.isEmpty() || pass!!.isEmpty()) {

            Toast.makeText(
                context, "Please check your user id and password", Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            return true
        }
    }

    fun toast(string: String, context: Context) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
    fun convertJpegToBitmap(jpegFilePath: String): Bitmap? {
        val jpegFile = File(jpegFilePath)

        if (jpegFile.exists()) {
            return BitmapFactory.decodeFile(jpegFilePath)
        } else {
            println("File does not exist")
            return null
        }
    }

    fun convertBitmapToJpeg(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }

}