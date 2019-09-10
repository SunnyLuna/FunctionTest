package com.decard.zj.founctiontest.widgets

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.Gravity
import android.view.WindowManager
import android.widget.EditText
import com.decard.zj.founctiontest.R
import com.example.commonlibs.pickview.ConvertUtils
import com.example.commonlibs.pickview.DatePicker
import kotlinx.android.synthetic.main.activity_number_key.*
import java.lang.reflect.Method


class NumberKeyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_key)
        disableShowSoftInput()
        number_key.setAttachToEditText(et_key)

        btn_timer.setOnClickListener {
            val datePicker = DatePicker(this)
            datePicker.setCanceledOnTouchOutside(true)
            datePicker.setUseWeight(true)
            datePicker.setGravity(Gravity.CENTER)
            datePicker.setTopPadding(ConvertUtils.toPx(this, 10f))
            datePicker.setTitleText("请选择时间")
            datePicker.setResetWhileWheel(true)
            datePicker.show()
        }
    }

    private fun disableShowSoftInput() {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            et_key.setInputType(InputType.TYPE_NULL)
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            val cls = EditText::class.java
            var method: Method
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
                method.isAccessible = true
                method.invoke(et_key, false)
            } catch (e: Exception) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", Boolean::class.javaPrimitiveType)
                method.isAccessible = true
                method.invoke(et_key, false)
            } catch (e: Exception) {
            }

        }
    }
}
