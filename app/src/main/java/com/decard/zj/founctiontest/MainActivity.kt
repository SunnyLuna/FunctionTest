package com.decard.zj.founctiontest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.decard.zj.founctiontest.a7s.RequestBean
import com.decard.zj.founctiontest.databinding.ActivityMainBinding
import com.decard.zj.founctiontest.network.RetrofitUtil
import com.decard.zj.founctiontest.utils.PreferenceUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity() {
    private val TAG = "---MainActivity"

    public var text: ObservableField<String> = ObservableField()

    private var data by PreferenceUtils("key", 0)

    val logger = LoggerFactory.getLogger("AutoStartBroadcastReceiver")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this
        logger.trace("activity启动")
        RetrofitUtil.getA7S().testA7S(RequestBean("94734010742Z06Z", "01000209", "391C4C80CDD851F791C890B74CAB18FB")).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Log.d(TAG, "onCreate: " + it.toString())
                }, {
                    Log.d(TAG, "onCreate: ${it.message}")
                })
    }

    public fun read(view: View) {
        Toast.makeText(this, "绑定点击", Toast.LENGTH_SHORT).show()
        text.set(data.toString())
    }

    public fun save(view: View) {
        data = (1..10).random()
    }
}
