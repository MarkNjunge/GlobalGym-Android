package com.marknkamau.globalgym.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

/**
 * A class to be used to easily handle dates.
 * Months begin at 1
 */
data class DateTime(var year: Int,
                    var month: Int,
                    var dayOfMonth: Int,
                    var hourOfDay: Int,
                    var minute: Int,
                    var second: Int = 0) {

    companion object {
        /**
         * Returns the current time as a DateTime object.
         */
        val now: DateTime
            get() = Date(System.currentTimeMillis()).toDateTime()

        /**
         * Converts a timestamp in seconds to a DateTime object
         */
        fun fromTimestamp(timestamp: Long): DateTime = Date(timestamp * 1000).toDateTime()

        const val APP_DATE_FORMAT = "E, dd MMMM"
        const val APP_TIME_FORMAT = "hh:mm a"
    }

    /**
     * Time in seconds.
     */
    val timestamp: Long
        get() {
            val now = Calendar.getInstance()
            now.set(this.year, this.month - 1, this.dayOfMonth, this.hourOfDay, this.minute, this.second)
            return now.time.time / 1000L
        }

    /**
     * Formats the dateTime as the given format
     */
    fun format(format: String): String {
        val now = Calendar.getInstance()
        now.set(this.year, this.month - 1, this.dayOfMonth, this.hourOfDay, this.minute, this.second)
        return now.time.format(format)
    }

}

/**
 * Converts a java date to a DateTime object.
 */
fun Date.toDateTime(): DateTime {
    val hourOfDay = this.format("H").toInt() // Format according to 24Hr from 0-23
    val minute = this.format("m").toInt()
    val year = this.format("yyyy").toInt()
    val month = this.format("M").toInt()
    val dayOfMonth = this.format("dd").toInt()
    val second = this.format("s").toInt()

    return DateTime(year, month, dayOfMonth, hourOfDay, minute, second)
}

/**
 * Helper function to format dates.
 */
fun Date.format(pattern: String): String = SimpleDateFormat(pattern).format(this)