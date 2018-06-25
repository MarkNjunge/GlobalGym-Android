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
        val dateTime = DateTime(2018, 1, 16, 8, 30, 0)
        Assert.assertEquals(1516080600, dateTime.unix)
    }

    @Test
    fun should_convertToTimestamp() {
        val dateTime = DateTime(2018, 1, 16, 8, 30, 0)
        Assert.assertEquals(1516080600000, dateTime.timestamp)
    }

    @Test
    fun should_convertFromUnix() {
        val fromUnix = DateTime.fromUnix(1516102715)

        Assert.assertEquals(2018, fromUnix.year)
        Assert.assertEquals(1, fromUnix.month)
        Assert.assertEquals(16, fromUnix.dayOfMonth)
        Assert.assertEquals(14, fromUnix.hourOfDay)
        Assert.assertEquals(38, fromUnix.minute)
        Assert.assertEquals(35, fromUnix.second)
    }

    @Test
    fun should_convertFromTimestamp() {
        val fromUnix = DateTime.fromTimestamp(1515941112000)

        Assert.assertEquals(2018, fromUnix.year)
        Assert.assertEquals(1, fromUnix.month)
        Assert.assertEquals(14, fromUnix.dayOfMonth)
        Assert.assertEquals(17, fromUnix.hourOfDay)
        Assert.assertEquals(45, fromUnix.minute)
        Assert.assertEquals(12, fromUnix.second)
    }

    @Test
    fun should_format() {
        val dateTime = DateTime(2001, 7, 4, 12, 8, 56)

        Assert.assertEquals("2001.07.04 AD at 12:08:56", dateTime.format("yyyy.MM.dd G 'at' HH:mm:ss"))
    }

    @Test
    fun should_getNow() {
        Assert.assertEquals(System.currentTimeMillis(), DateTime.now.timestamp)
    }
}