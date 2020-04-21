package com.example.commonlibs.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Test
    public void getCurrentDate() {

        assertEquals("20191202", DateUtils.getCurrentDate());
    }

    @Test
    public void getCurrentTime() {
        assertEquals("2019年12月02日 10:40:20", DateUtils.getCurrentTime());
    }

    @Test
    public void dateToFormat() {
        long time = 1574650030000L;
        assertEquals("2019年11月25日 10:47:10", DateUtils.dateToFormat(time));
    }

    @Test
    public void getWeekOfDate() {

        assertEquals("星期一", DateUtils.getWeekOfDate(new Date()));
    }

    @Test
    public void getDayOfMonth() {
        assertEquals(31, DateUtils.getDayOfMonth(2019,12));
    }
}