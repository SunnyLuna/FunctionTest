package com.decard.zj.founctiontest.picture

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.decard.zj.founctiontest.R
import kotlinx.android.synthetic.main.activity_glide.*
import javax.xml.transform.Source

class GlideActivity : AppCompatActivity() {


    private val url = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg"
    private val yanUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539162720450&di=828046999a8f8a601cd8eb10945a99d0&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn22%2F600%2Fw1920h1080%2F20180728%2Fcc2d-hfxsxzh1435807.jpg"
    private val ziUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539757578&di=ae3134e35b763f4e4e5a3ee3ff618a35&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201608%2F10%2F20160810110214_jRBJC.jpeg"
    private val lanUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539163980994&di=c566a867b85aed82f3039d927a20241d&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201511%2F23%2F20151123154544_SY3Ww.jpeg"


    private val target: SimpleTarget<Bitmap> = object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            iv_picture.setImageBitmap(resource)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)
        /**
         * 显示图片，自定义target，获取图片数据
         */
        btn_glide_show.setOnClickListener {

            val options = RequestOptions()
                    .placeholder(R.mipmap.timg)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
            Glide.with(this)
                    .asBitmap()
                    .load(lanUrl)
                    .apply(options)
                    .into(target)

            Glide.with(this)
                    .load(lanUrl)
                    .into(my_layout.getTarget())

        }
        /**
         * 使用预加载preload,最好将diskcachestrategy的缓存策略设置成source，
         * 因为preload（）方法默认是预加载的原始图片大小，
         *     而into()方法默认会根据imageview的大小动态决定加载图片的大小
         */
        val options = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)
        Glide.with(this)
                .load(yanUrl)
                .apply(options)
                .preload()
        pre_load.setOnClickListener {
            //仍然需要使用diskcachestrategy()方法设置缓存策略为source，保证glide一定会读取预加载的图片缓存
            Glide.with(this)
                    .load(yanUrl)
                    .apply(options)
                    .into(iv_picture)

        }

        /**
         * download
         */
        download.setOnClickListener {

        }

        /**
         * TargetDownload
         */

        target_download.setOnClickListener {

        }
    }
}
