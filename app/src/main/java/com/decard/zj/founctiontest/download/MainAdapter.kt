package com.decard.zj.founctiontest.download

import android.app.Activity
import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.decard.zj.founctiontest.R
import com.decard.zj.kotlinbaseapplication.utils.RxBusInner
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @Author: liuwei
 *
 * @Create: 2019/5/20 14:33
 *
 * @Description:

 */
class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private val TAG = MainAdapter::class.java.simpleName
    private var mItemClickListener: OnItemClickListener? = null
    private var mJumpToAppDetailListener: JumpToAppDetailListener? = null
    private var mContext: Activity? = null
    private var mDataList: ArrayList<DataBean>? = null
    private var postMark = ""

    fun setData(context: Activity, postMark: String, dataList: ArrayList<DataBean>) {
        mContext = context
        mDataList = dataList
        this.postMark = postMark
        val dispose = RxBusInner.getInstance().toObservable(DownloadBean::class.java)
                .observeOn(AndroidSchedulers.mainThread()).subscribe { downloadBean ->
                    mDataList!!.forEach {
                        if (it.apkName == downloadBean.apkName) {
                            it.status = downloadBean.status
                            it.progress = downloadBean.progress
                            notifyDataSetChanged()
                        }
                    }
                }
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_main,
                parent, false
        )
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mDataList.isNullOrEmpty()) {
            0
        } else {
            mDataList!!.size
        }
    }

    override fun onBindViewHolder(view: MainViewHolder, position: Int) {
        val data = mDataList!![position]
        view.itemView.tag = position
        view.name.text = data.apkName
        view.description.text = data.apkInfo
        view.version.text = "版本" + data.apkVersion
        view.size.text = data.apkSize + "M"
        view.progressBtn.text = data.status
        if (data.progress == -1) {
            view.progressBtn.text = data.status
            view.progressBtn.setProgress(0)
        } else {
            if (data.status == DownloadStatus.PAUSE) {
                view.progressBtn.text = data.status
                view.progressBtn.setProgress(0)
            } else {
                view.progressBtn.setProgress(data.progress)
                view.progressBtn.text = "${data.progress}%"
            }
        }
        Glide.with(mContext!!)
                .asBitmap()
                .load(data.imageUrl)
                .error(android.R.color.darker_gray)
                .placeholder(android.R.color.darker_gray)
                .fitCenter()
                .centerCrop()
                .into(object : BitmapImageViewTarget(view.image) {
                    override fun setResource(resource: Bitmap?) {
                        val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext!!.resources, resource)
                        circularBitmapDrawable.cornerRadius = 10f //设置圆角弧度
                        view.image.setImageDrawable(circularBitmapDrawable)
                    }
                })

        view.progressBtn.setOnClickListener {
            DownloadManager.addTask(data)

        }

        view.item.setOnClickListener {
            mJumpToAppDetailListener?.jump(data)
        }
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.item_main_logo)
        val name = itemView.findViewById<TextView>(R.id.item_main_name)
        val description = itemView.findViewById<TextView>(R.id.item_main_description)
        val version = itemView.findViewById<TextView>(R.id.item_main_version)
        val size = itemView.findViewById<TextView>(R.id.item_main_size)
        val progressBtn = itemView.findViewById<DownloadProgressButton>(R.id.item_progress_button)
        val item = itemView.findViewById<RelativeLayout>(R.id.item_main)
    }


    interface OnItemClickListener {
        fun onItemClick(data: DataBean)
    }

    interface JumpToAppDetailListener {
        fun jump(data: DataBean)
    }
}