package com.decard.zj.founctiontest.network.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.zj.founctiontest.R
import com.decard.zj.founctiontest.network.base.RetrofitCoroutineUtils
import kotlinx.android.synthetic.main.activity_coroutine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class CoroutineActivity : AppCompatActivity() {

    private val TAG = "---CoroutineActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)
        btn_send.setOnClickListener {
            loadData()

        }
    }

    private fun loadData() = runBlocking {
        val content = try {
            RetrofitCoroutineUtils.getUpload().getNames().await()
        } catch (e: Exception) {
            e.message.toString()
            Log.d(TAG, "loadData: ${e.message}")
        }
        Log.d(TAG, "loadData: $content")
        withContext(Dispatchers.Main) {
            tv_content.text = content.toString()
        }
    }
}
