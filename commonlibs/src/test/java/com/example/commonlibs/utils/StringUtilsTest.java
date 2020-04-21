package com.example.commonlibs.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    @Test
    public void isEmpty() {
        assertTrue(StringUtils.isEmpty(""));
    }

    @Test
    public void isEmail() {
        assertTrue(StringUtils.isEmail("fddddddd@ddd163.com"));
    }

    @Test
    public void isPhoneNumberValid() {
        assertTrue(StringUtils.isPhoneNumberValid("18345678910"));
    }

    @Test
    public void isPhoneNumberValid1() {
        assertTrue(StringUtils.isPhoneNumberValid("+86","18345678910"));
    }

    @Test
    public void isPhoneFormat() {

    }

    @Test
    public void isNumber() {
        assertTrue(StringUtils.isNumber("100000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void isMobileSimple() {
    }

    @Test
    public void isMobileExact() {
    }

    @Test
    public void isTel() {
    }

    @Test
    public void isIdCard() {
    }

    @Test
    public void isUrl() {
    }

    @Test
    public void isDate() {
    }

    @Test
    public void isIp() {
    }

    @Test
    public void isCar() {
    }

    @Test
    public void isName() {
    }

    @Test
    public void isMac() {
    }

    @Test
    public void isMatch() {
    }

    @Test
    public void identityCardVerification() {
    }

    @Test
    public void main() {
    }

    @Test
    public void checkIdCard() {
    }

    @Test
    public void isStrDate() {
    }

    @Test
    public void addZeroForString() {
    }

    @Test
    public void addZeroForNum() {
    }

    @Test
    public void concatAll() {
    }

    @Test
    public void addZeroForNum1() {
    }

    @Test
    public void addSpaceForStringRight() {
    }

    @Test
    public void addSpaceForStringLeft() {
    }

    @Test
    public void setSpace() {
    }
}