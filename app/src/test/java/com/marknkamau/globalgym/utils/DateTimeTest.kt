package com.marknkamau.globalgym.utils

import org.junit.Assert
import org.junit.Test

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class DateTimeTest{

    @Test
    fun should_convertToUnix() {
        val dateTime = DateTime(2018, 1, 1, 8, 30, 0)
        Assert.assertEquals(1514784600, dateTime.timestamp)
    }

    @Test
    fun should_convertFromUnix() {
        val fromUnix = DateTime.fromTimestamp(1514784600)

        Assert.assertEquals(2018, fromUnix.year)
        Assert.assertEquals(1, fromUnix.month)
        Assert.assertEquals(1, fromUnix.dayOfMonth)
        Assert.assertEquals(8, fromUnix.hourOfDay)
        Assert.assertEquals(30, fromUnix.minute)
        Assert.assertEquals(0, fromUnix.second)
    }

    @Test
    fun should_format() {
        val dateTime = DateTime(2001, 7, 4, 12, 8, 56)

        Assert.assertEquals("2001.07.04 AD at 12:08:56", dateTime.format("yyyy.MM.dd G 'at' HH:mm:ss"))
    }

    @Test
    fun should_getNow() {
        Assert.assertEquals(System.currentTimeMillis() / 1000, DateTime.now.timestamp)
    }
}