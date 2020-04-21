package com.decard.zj.founctiontest

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.decard.zj.founctiontest.databinding.ActivityMainBinding
import com.decard.zj.founctiontest.utils.PreferenceUtils

class MainActivity : AppCompatActivity() {

    public var text: ObservableField<String> = ObservableField()

    private var data by PreferenceUtils("key", 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this
    }

    public fun read(view: View) {
        Toast.makeText(this, "绑定点击", Toast.LENGTH_SHORT).show()
        text.set(data.toString())
    }

    public fun save(view: View) {
        data = (1..10).random()
    }
}
