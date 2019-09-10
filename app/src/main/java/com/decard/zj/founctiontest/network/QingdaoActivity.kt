package com.decard.zj.founctiontest.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import com.decard.zj.founctiontest.R
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_qingdao.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*

class QingdaoActivity : AppCompatActivity() {

    val TAG = "----QingdaoActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qingdao)

        btn_social.setOnClickListener {
            //获取社保卡编号
//            getSoicalNumber()

        }

        btn_photo.setOnClickListener {
            //获取社保卡照片
//            getSocialPhoto()
            var social = "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"
            val decode = Base64.decode(social, 0)
            iv_social.setImageBitmap(convertStringToIcon(decode))

        }

        btn_upload.setOnClickListener {
            val basePath = Environment.getExternalStorageDirectory().absolutePath + "/timg.png"
            uploadTest(basePath)
        }
    }


    private fun getSocialPhoto() {
        RetrofitUtil.getSocialMap().getSocialPhoto(tv_social.text.toString()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<String> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: String) {
                        tv_photo.text = t
                        val decode = Base64.decode(t, 0)
                        iv_social.setImageBitmap(convertStringToIcon(decode))

                    }

                    override fun onError(e: Throwable) {
                        tv_photo.text = e.toString()
                    }
                })
    }

    private fun getSoicalNumber() {
        RetrofitUtil.getSocialNumber().getSocialNumber("370283198912164115").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<SocialBean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: SocialBean) {
                        tv_social.text = t.AAC001
                    }

                    override fun onError(e: Throwable) {
                        tv_social.text = e.toString()
                    }
                })
        DataSource().initAsync("", object : LoadTasksCallback {
            override fun onGetName(message: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTasksLoaded(tasks: SocialBean) {

            }

            override fun onDataNotAvailable(msg: String) {

            }

        })
    }


    /**
     * 上传照片
     */
    private fun uploadTest(path: String) {
        val file = File(path)

        Log.d(TAG, "path: $path")
        Log.d(TAG, "onClick: " + file.name)
        val strBase64 = fileToBase64(file)
        val photoRequestBody: RequestBody = RequestBody.create(MediaType.parse("img/png"), file)
        val photo: MultipartBody.Part = MultipartBody.Part.createFormData("fileData", file.name, photoRequestBody)

        val params = HashMap<String, RequestBody>()
        params["authCode"] = toRequestBody("5bab39adddc2435782e5f214e3d79733")
        params["objID"] = toRequestBody("410104199901018481")
        params["objNam"] = toRequestBody("材料增二")
        params["bizProcCod"] = toRequestBody("sdfsd")
        params["bizProcNam"] = toRequestBody("考核表")
        params["fileTypeCod"] = toRequestBody("sfz")
        params["fileTypeNam"] = toRequestBody("考核表")
        params["fileFormat"] = toRequestBody("PNG")
        params["fileName"] = toRequestBody(file.name)
        params["fileID"] = toRequestBody("")
        params["fileBase64"] = toRequestBody(strBase64!!)
        params["fileLabel"] = toRequestBody("5bab39adddc2435782e5f214e3d79733")

        RetrofitUtil.getUpload().upload(params, photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ReturnBean> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(dataBean: ReturnBean) {
                        tv_upload.text = dataBean.rtnCode + dataBean.rtnMsg
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "onError: " + e.toString())
                        tv_upload.text = "onError: " + e.toString()
                    }

                    override fun onComplete() {
                        Log.d(TAG, "onComplete: ")
                    }
                })
    }


    private fun toRequestBody(str: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), str)
    }

    private fun fileToBase64(file: File): String? {
        var base64: String? = null
        var `in`: InputStream? = null
        try {
            `in` = FileInputStream(file)
            val bytes = ByteArray(`in`.available())
            val length = `in`.read(bytes)
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT)
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } finally {
            try {
                `in`?.close()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        return base64
    }


    fun convertStringToIcon(bmpStr: ByteArray): Bitmap? {
        return try {
            val bitmap = BitmapFactory.decodeByteArray(bmpStr, 0,
                    bmpStr.size)
            bitmap
        } catch (e: Exception) {
            null
        }

    }
}
