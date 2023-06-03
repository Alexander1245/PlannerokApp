package com.dart69.plannerokapp.profile.data

import java.text.SimpleDateFormat
import java.util.Locale

interface DateFormatter {
    /**
     * @return epoch millis
     * */
    fun format(dateString: String): Long

    class Implementation(
        private val pattern: String = "yyyy-MM-dd",
        private val locale: Locale = Locale("RU"),
    ) : DateFormatter {
        override fun format(dateString: String): Long {
            val format = SimpleDateFormat(pattern, locale)
            return requireNotNull(format.parse(dateString)?.time) { "Unknown format" }
        }
    }
}