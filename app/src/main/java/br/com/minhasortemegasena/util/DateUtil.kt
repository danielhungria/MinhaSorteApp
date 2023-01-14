package br.com.minhasortemegasena.util

import android.util.Log
import br.com.minhasortemegasena.core.Constants
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun formatDateFromAPI(dateToFormat: String?, datePattern: String?): String {
        val dateTimeFormat = SimpleDateFormat(Constants.API_DATE_PATTERN, Locale.getDefault())
        dateTimeFormat.timeZone = TimeZone.getTimeZone(Constants.UTC)
        return if (dateToFormat != null) {
            try {
                val date = dateTimeFormat.parse(dateToFormat)
                val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
                sdf.timeZone = TimeZone.getDefault()
                if (date != null) sdf.format(date) else ""
            } catch (e: Exception) {
                Log.e("FormatError", e.message.toString())
                ""
            }
        } else {
            ""
        }
    }

}