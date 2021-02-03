package com.airongomes.compassouol.util

import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Formatar data para ser mostrada na TextView
 */
fun formatDateTime(cal: Calendar, context: Context): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        val dateFormat = LocalDateTime.of(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE))
        dateFormat.format(dateTimeFormatter)
    } else{
        val dateFormatter = DateFormat.getLongDateFormat(context)
        dateFormatter.format(cal.time)
    }
}