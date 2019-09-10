package com.example.commonlibs.utils

import android.content.Context
import android.util.TypedValue

/**
 * 单位转换工具类
 * @author ZJ
 * created at 2019/4/26 14:58
 */
object DensityUtils {

    /**
     * dp转px
     */
    fun dp2px(context: Context, dpVal: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics)
    }

    /**
     * sp转px
     */
    fun sp2px(context: Context, spVal: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.resources.displayMetrics)
    }

    /**
     * px转dp
     */
    fun px2dp(context: Context, pxVal: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (pxVal / scale)
    }

    /**
     * px转sp
     */
    fun px2sp(context: Context, pxVal: Float): Float {
        return (pxVal / context.resources.displayMetrics.scaledDensity)
    }

}