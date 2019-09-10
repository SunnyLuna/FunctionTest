package com.example.commonlibs.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


/**
 * 自定义popwindow
 * @author ZJ
 * created at 2019/5/7 17:52
 */
class CustomPopWindow(context: Context) {

    var mContext: Context = context
    private var mWidth = 0
    private var mHeight = 0
    private var mIsFocusable = false
    private var mIsOutSide = false
    private var mReslayoutId = -1
    private var mContentView: View? = null
    private var mPopWindow: PopupWindow? = null
    private var mAnimationStyle = -1
    private var mClipEnable = true
    private var mIgnoreCheekPress = false
    private var mInputMode = -1
    private var mOnDismissListener: PopupWindow.OnDismissListener? = null
    private var mSoftInputMode = -1
    private var mTouchable = true
    private var mOnTouchListener: View.OnTouchListener? = null

    //当前activity的窗口
    private var mWindow: Window? = null
    private var mIsBackgroundDark = false  //背景是否变暗
    private var mBackgrondDarkValue = 0.5f//变暗的值  范围 0---1


    fun showAsDropDown(anchor: View): CustomPopWindow {
        if (mPopWindow != null) {
            mPopWindow!!.showAsDropDown(anchor)
        }
        return this
    }

    fun showAsDropDown(anchor: View, xOff: Int, yOff: Int): CustomPopWindow {
        if (mPopWindow != null) {
            mPopWindow!!.showAsDropDown(anchor, xOff, yOff)
        }
        return this
    }

    fun showAsDropDown(anchor: View, xOff: Int, yOff: Int, gravity: Int): CustomPopWindow {
        if (mPopWindow != null) {
            mPopWindow!!.showAsDropDown(anchor, xOff, yOff, gravity)
        }
        return this
    }

    /**
     *  相对于父控件的位置
     *  （通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
     */
    fun showAtLocation(parent: View, gravity: Int, xOff: Int, yOff: Int): CustomPopWindow {
        mPopWindow!!.showAtLocation(parent, gravity, xOff, yOff)
        return this
    }

    /**
     * 添加一些属性设置
     */
    private fun apply(popupWindow: PopupWindow) {
        popupWindow.isClippingEnabled = mClipEnable
        if (mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress()
        }
        if (mInputMode != -1) {
            popupWindow.inputMethodMode = mInputMode
        }
        if (mSoftInputMode != -1) {
            popupWindow.softInputMode = mSoftInputMode
        }
        if (mOnDismissListener != null) {
            popupWindow.setOnDismissListener(mOnDismissListener)
        }
        if (mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(mOnTouchListener)
        }
        popupWindow.isTouchable = mTouchable
    }

    private fun build(): PopupWindow {
        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(mReslayoutId, null)
        }
        //获取当前activity的窗口
        val activity = mContentView!!.context as Activity
        if (mIsBackgroundDark) {
            mWindow = activity.window
            val params = mWindow!!.attributes
            params.alpha = mBackgrondDarkValue
            mWindow!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            mWindow!!.attributes = params
        }

        if (mWidth != 0 && mHeight != 0) {
            mPopWindow = PopupWindow(mContentView, mWidth, mHeight)
        } else {
            mPopWindow = PopupWindow(mContentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }

        if (mAnimationStyle != -1) {
            mPopWindow!!.animationStyle = mAnimationStyle
        }
        apply(mPopWindow!!)//设置属性
        mPopWindow!!.isFocusable = mIsFocusable
        mPopWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mPopWindow!!.isOutsideTouchable = mIsOutSide
        if (mWidth == 0 || mHeight == 0) {
            //如果外边没有设置宽高的话，计算宽高并赋值
            mPopWindow!!.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            mWidth = mPopWindow!!.contentView.measuredWidth
            mHeight = mPopWindow!!.contentView.measuredHeight
        }
        mPopWindow!!.update()
        return mPopWindow!!
    }

    class PopWindowBuilder(context: Context) {
        var mCustomPopWindow: CustomPopWindow = CustomPopWindow(context)


        fun size(width: Int, height: Int): PopWindowBuilder {
            mCustomPopWindow.mWidth = width
            mCustomPopWindow.mHeight = height
            return this
        }

        fun setFocusable(focusable: Boolean): PopWindowBuilder {
            mCustomPopWindow.mIsFocusable = focusable
            return this
        }

        fun setView(resLayoutId: Int): PopWindowBuilder {
            mCustomPopWindow.mReslayoutId = resLayoutId
            mCustomPopWindow.mContentView = null
            return this
        }

        fun setView(view: View): PopWindowBuilder {
            mCustomPopWindow.mReslayoutId = -1
            mCustomPopWindow.mContentView = view
            return this
        }

        fun setOutSideTouchable(outSideTouchable: Boolean): PopWindowBuilder {
            mCustomPopWindow.mIsOutSide = outSideTouchable
            return this
        }

        /**
         * 设置弹窗动画
         */
        fun setAnimationStyle(animationStyle: Int): PopWindowBuilder {
            mCustomPopWindow.mAnimationStyle = animationStyle
            return this
        }

        fun setClippingEnable(enable: Boolean): PopWindowBuilder {
            mCustomPopWindow.mClipEnable = enable
            return this
        }

        fun setIgnoreCheekPress(ignoreCheekPress: Boolean): PopWindowBuilder {
            mCustomPopWindow.mIgnoreCheekPress = ignoreCheekPress
            return this
        }

        fun setInputMethodMode(mode: Int): PopWindowBuilder {
            mCustomPopWindow.mInputMode = mode
            return this
        }

        fun setOnDissmissListener(onDissmissListener: PopupWindow.OnDismissListener): PopWindowBuilder {
            mCustomPopWindow.mOnDismissListener = onDissmissListener
            return this
        }

        fun setSoftInputMode(softInputMode: Int): PopWindowBuilder {
            mCustomPopWindow.mSoftInputMode = softInputMode
            return this
        }

        fun setTouchable(touchable: Boolean): PopWindowBuilder {
            mCustomPopWindow.mTouchable = touchable
            return this
        }

        fun setTouchIntercepter(touchIntercepter: View.OnTouchListener): PopWindowBuilder {
            mCustomPopWindow.mOnTouchListener = touchIntercepter
            return this
        }

        fun autoDismiss(): PopWindowBuilder {
            Observable.timer(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
                mCustomPopWindow.dismiss()
            }
            return this
        }

        /**
         * 设置背景是否变暗
         */
        fun setBackgroundDark(isDark: Boolean): PopWindowBuilder {
            mCustomPopWindow.mIsBackgroundDark = isDark
            return this
        }

        /**
         * 设置背景变暗的值
         */
        fun setBackgroundDarkAlpha(alpha: Float): PopWindowBuilder {
            mCustomPopWindow.mBackgrondDarkValue = alpha
            return this
        }

        fun create(): CustomPopWindow {
            //构建PopWindow
            mCustomPopWindow.build()
            return mCustomPopWindow
        }
    }

    /**
     * 关闭popwindow
     */
    public fun dismiss() {
        if (mIsBackgroundDark) {
            val params = mWindow!!.attributes
            params.alpha = 1.0f
            mWindow!!.attributes = params
        }
        if (mPopWindow != null) {
            mPopWindow!!.dismiss()
        }
    }

}