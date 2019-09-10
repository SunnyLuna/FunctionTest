package com.decard.zj.founctiontest

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.decard.zj.founctiontest.databinding.ActivityMainBinding
import com.example.commonlibs.utils.ResourcesUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    public var text: ObservableField<String> = ObservableField()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding.main = this
    }

    public fun go(view: View) {
        Toast.makeText(this, "绑定点击", Toast.LENGTH_SHORT).show()
        tv_show.text = "应该是绑定成功了"
    }


}
