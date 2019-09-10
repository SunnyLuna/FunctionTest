package com.decard.zj.founctiontest.picture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition

/**
 * File Description
 * @author Dell
 * @date 2018/10/11
 *
 */
class MyLayout : LinearLayout {


    private var viewTarget: ViewTarget<MyLayout, Drawable>? = null

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        viewTarget = object : ViewTarget<MyLayout, Drawable>(this) {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val myLayout = getView()
                myLayout.background = resource
            }
        }
    }

    fun getTarget(): ViewTarget<MyLayout, Drawable> {
        return viewTarget!!
    }
}