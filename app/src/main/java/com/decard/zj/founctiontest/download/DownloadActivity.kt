package com.decard.zj.founctiontest.download

import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import android.util.Log
import com.decard.appstore.net.responsebean.InitResponse
import com.decard.zj.founctiontest.R
import com.decard.zj.founctiontest.TestApplication
import com.example.commonlibs.utils.ScreenUtils
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_download.*
import okhttp3.RequestBody
import java.util.*


class DownloadActivity : AppCompatActivity() {


    val TAG = "----------download"
    val baseUrl = "http://47.105.35.37:19092"

    companion object {
        var postMark = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        Log.d("+++++++++++++++++", ScreenUtils.getSmallestWidthDP(this).toString())
//        Log.d("+++++++++++++++++", TTSUtils.sHA1(this))
        RxPermissions(this).request(android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe {
            if (it) {
                initData()
                initView()
            }
        }
    }

    val adapter = MainAdapter()
    private fun initView() {
//        val layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        main_list!!.layoutManager = layoutManager
        main_list!!.adapter = adapter
        (main_list!!.itemAnimator as androidx.recyclerview.widget.SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun initData() {
        val gson = Gson()
        val initRequestBean = InitRequestBean(
                Settings.Secure.getString(
                        TestApplication.instance.contentResolver,
                        Settings.Secure.ANDROID_ID
                ),
                Build.MODEL,
                "108.88576,34.22455"
        )
        val requestBody = gson.toJson(initRequestBean)
        val body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestBody)

        RetrofitUtils.getData().getPostMark(body).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(object : Observer<BaseResponse<InitResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: BaseResponse<InitResponse>) {
                        Log.d(TAG, t.result.toString())
                        postMark = t.result.postmark
                        getAllData()
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, e.message)
                    }
                })
    }

    private fun getAllData() {
        RetrofitUtils.getData().getAllApp(postMark).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResponse<List<AllAppInfoResponse>>> {
                    override fun onError(e: Throwable) {
                        Log.d(TAG, e.message)
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: BaseResponse<List<AllAppInfoResponse>>) {
                        val allApps = t.result
                        val listBean = ArrayList<DataBean>()


                        allApps.forEach {
                            val dataBean = DataBean()
                            dataBean.taskId = it.software_id + it.software_version
                            dataBean.fileMD5 = it.file_md5
                            dataBean.softwareId = it.software_id
                            dataBean.apkName = it.software_name
                            dataBean.apkVersion = it.software_version
                            dataBean.apkInfo = it.software_sketch
                            dataBean.apkSize = String.format("%.2f", (it.software_size.toDouble() / 1024))
                            dataBean.imageUrl = "$baseUrl${it.icon_url}"
                            dataBean.packetName = it.software_package_name
                            listBean.add(dataBean)
                        }
                        val allData = DBUtil.queryAll()
                        Log.d("----------数据库", allData.size.toString())
                        allData.forEach {
                            Log.d("----------数据库", it.toString())
                        }
                        listBean.forEach {
                            allData.forEach { db ->
                                if (db.taskId == it.taskId) {
                                    it.progress = db.progress
                                    it.status = db.status
                                    it.downloadSize = db.downloadSize
                                    it.filePath = db.filePath
                                }
                            }
                        }
                        listBean.forEach {
                            Log.d("----------", it.toString())
                        }
                        adapter.setData(this@DownloadActivity, postMark, listBean)
                    }
                })

    }

    override fun onStop() {
        super.onStop()
        DownloadManager.stopAllTask()
    }

    override fun onDestroy() {
        super.onDestroy()
        Realm.getDefaultInstance().close()
    }

}


