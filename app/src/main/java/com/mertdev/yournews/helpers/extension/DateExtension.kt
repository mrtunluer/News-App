package com.mertdev.yournews.helpers.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.convertToCustomDateFormat(): String {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    val date = inputFormat.parse(this) ?: return ""
    val yearFormat =
        if (SimpleDateFormat("yyyy", Locale.US).format(date).toInt() == currentYear) "" else " yy"
    val outputFormat = SimpleDateFormat("dd MMM$yearFormat", Locale.US)
    return outputFormat.format(date)
}
