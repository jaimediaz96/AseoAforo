package com.example.aseoaforos.util

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtil {
    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    fun formatDate(inputDate: Date): String {
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val calendar = outputFormat.calendar
        calendar.time = inputDate

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = DateFormatSymbols.getInstance(Locale.getDefault())
            .months[calendar.get(Calendar.MONTH)]
        val year = calendar.get(Calendar.YEAR)

        return String.format(Locale.getDefault(), "%02d %s %d", day, month, year)
    }

    fun formatTime(inputDate: Date): String {
        val formatHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        return formatHora.format(inputDate)
    }

    fun getDayOfWeek(inputDate: Date): String {
        val dayNames = arrayOf(
            "Domingo",
            "Lunes",
            "Martes",
            "Miércoles",
            "Jueves",
            "Viernes",
            "Sábado"
        )

        val calendar = Calendar.getInstance()
        calendar.time = inputDate

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayNames[dayOfWeek - 1]
    }
}