package com.example.commonlibs.utils

object KeyUtils {

    fun getKeyValue(keycode: Int): String {
        var value = ""
        when (keycode) {
            14 -> value = "7"
            15 -> value = "8"
            16 -> value = "9"
            67 -> value = "delete"
            11 -> value = "4"
            12 -> value = "5"
            13 -> value = "6"
            111 -> value = "cancel"
            8 -> value = "1"
            9 -> value = "2"
            10 -> value = "3"
            66 -> value = "OK"
            56 -> value = "."
            7 -> value = "0"
//            154 -> value = "/"
//            155 -> value = "*"
//            157 -> value = "+"
//            69 -> value = "-"
        }
        return value
    }


}