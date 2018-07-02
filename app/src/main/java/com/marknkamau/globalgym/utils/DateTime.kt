package com.marknkamau.globalgym.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

data class DateTime(var year: Int,
                    var month: Int,
                    var dayOfMonth: Int,
                    var hourOfDay: Int,
                    var minute: Int,
                    var second: Int = 0,
                    var millisecond: Int = 0) {

    companion object {
        val now: DateTime
            get() = System.currentTimeMillis().toDateTime()

        private fun Long.toDateTime() = Date(this).toDateTime()

        fun fromTimestamp(timestamp: Long): DateTime = Date(timestamp).toDateTime()
        fun fromUnix(timestamp: Long): DateTime = Date(timestamp * 1000).toDateTime()

        const val APP_DATE_FORMAT = "dd - MMM - YY"
        const val APP_TIME_FORMAT = "hh:mm a"
    }

    val unix: Long
        get() {
            val now = Calendar.getInstance()
            // Month starts at 0
            now.set(this.year, this.month, this.dayOfMonth, this.hourOfDay, this.minute, this.second)
            return now.time.time / 1000L
        }

    val timestamp: Long
        get() {
            val now = Calendar.getInstance()
            // Month starts at 0
            now.set(this.year, this.month, this.dayOfMonth, this.hourOfDay, this.minute, this.second)
            now.set(Calendar.MILLISECOND, this.millisecond)
            return now.time.time
        }

    fun format(format: String): String {
        val now = Calendar.getInstance()
        // Month starts at 0
        now.set(this.year, this.month, this.dayOfMonth, this.hourOfDay, this.minute, this.second)
        return now.time.format(format)
    }

}

fun Date.toDateTime(): DateTime {
    val hourOfDay = this.format("H").toInt() // Format according to 24Hr from 0-23
    val minute = this.format("m").toInt()
    val year = this.format("yyyy").toInt()
    val month = this.format("M").toInt() - 1
    val dayOfMonth = this.format("dd").toInt()
    val second = this.format("s").toInt()
    val millisecond = this.format("S").toInt()

    return DateTime(year, month, dayOfMonth, hourOfDay, minute, second, millisecond)
}

fun Date.format(pattern: String): String = SimpleDateFormat(pattern).format(this)