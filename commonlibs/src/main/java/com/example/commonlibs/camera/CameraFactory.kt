package com.example.commonlibs.camera

/**
 * camera 工厂类
 * @author ZJ
 * created at 2019/5/17 15:56
 */
object CameraFactory {

    fun openCamera( version: Int): CameraUtils {
        return if (version == 1) {
            CustomCamera1.instance
        } else {
            CustomCamera2.instance
        }
    }

}