package com.example.aseoaforos.util

import android.content.Context
import android.widget.Toast
import com.example.aseoaforos.R

object Validator {
    private val regexEmail = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun email(context: Context, email: String): Boolean {
        return if (!regexEmail.matches(email)) {
            Toast.makeText(
                context,
                context.getString(R.string.errorEmail),
                Toast.LENGTH_LONG
            ).show()
            false
        } else {
            true
        }
    }

    private val regexPlate = Regex("^[A-Z]{3}\\d{3}\$")

    fun plate(context: Context, plate: String): Boolean {
        return if (!regexPlate.matches(plate)) {
            Toast.makeText(
                context,
                context.getString(R.string.errorPlate),
                Toast.LENGTH_LONG
            ).show()
            false
        } else {
            true
        }
    }

    fun notEmptyField(context: Context, text: String, field: Int): Boolean {
        return if (text.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.errorNoField) + "\"${context.getString(field)}\"",
                Toast.LENGTH_LONG
            ).show()
            false
        } else {
            true
        }
    }

    fun graterThanZero(context: Context, number: Double, field: Int): Boolean {
        return if (number <= 0) {
            Toast.makeText(
                context,
                context.getString(R.string.errorNumber) + "\"${context.getString(field)}\"",
                Toast.LENGTH_LONG
            ).show()
            false
        } else {
            true
        }
    }

    fun graterThanZero(context: Context, number: Long, field: Int): Boolean {
        return if (number <= 0) {
            Toast.makeText(
                context,
                context.getString(R.string.errorNumber) + "\"${context.getString(field)}\"",
                Toast.LENGTH_LONG
            ).show()
            false
        } else {
            true
        }
    }

    fun notEmptyPhotoList(context: Context, photoList: MutableList<String>): Boolean {
        return if (photoList.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.errorNoPhoto),
                Toast.LENGTH_LONG
            ).show()
            false
        } else {
            true
        }
    }
}