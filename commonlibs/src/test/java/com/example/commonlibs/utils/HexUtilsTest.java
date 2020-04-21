package com.example.commonlibs.utils;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HexUtilsTest {

    @Test
    public void toHexString() {
        byte b = 0x01;
        assertEquals("01", HexUtils.toHexString(b));
    }

    @Test
    public void toHexString1() {
        byte[] bytes = new byte[]{0x00, (byte) 0x01, (byte) 0xA2, (byte) 0xB3, (byte) 0xC4, (byte) 0xD5, (byte) 0xE6, (byte) 0xF7};
        assertEquals("0001A2B3C4D5E6F7", HexUtils.toHexString(bytes));
    }

    @Test
    public void toHexString2() {
        byte[] bytes = new byte[]{0x00, (byte) 0x01, (byte) 0xA2, (byte) 0xB3, (byte) 0xC4, (byte) 0xD5, (byte) 0xE6, (byte) 0xF7};
        assertEquals("0001A2B3C4D5E6F7", HexUtils.toHexString(bytes, 0, bytes.length));
    }

    @Test
    public void toHexString3() {
        int i = 10;
        assertEquals("0000000A", HexUtils.toHexString(i));
    }

    @Test
    public void toByteArray() {
        byte[] bytes = new byte[1];
        bytes[0] = 5;
        assertArrayEquals(bytes, HexUtils.toByteArray((byte) 5));
    }

    @Test
    public void toByteArray1() {
        int num = 128;
        byte[] bytes = new byte[4];
        //通过移位运算，截取低8位的方式，将int保存到byte数组
        bytes[0] = (byte) (num >>> 24);
        bytes[1] = (byte) (num >>> 16);
        bytes[2] = (byte) (num >>> 8);
        bytes[3] = (byte) num;
        System.out.println(num & 0xFF);
        System.out.println((num >> 8) & 0xFF);
        System.out.println((num >> 16) & 0xFF);
        System.out.println((num >> 24) & 0xFF);
        assertArrayEquals(bytes, HexUtils.toByteArray(128));
    }


    @Test
    public void hexStrToStr() {
        String s = "31";
        assertEquals("1", HexUtils.hexStrToStr(s));
    }

    @Test
    public void hexStringToBytes() {

    }

    @Test
    public void toByte() {
    }

    @Test
    public void hexStringToInt() {
        assertEquals(49, HexUtils.HexStringToInt("31"));
    }

    @Test
    public void intToHexString() {
        assertEquals("4e", HexUtils.IntToHexString(78));
    }

    @Test
    public void hexStringToBcd() {

    }
}