package com.decard.zj.founctiontest.calendar

import android.os.Bundle
import android.view.View
import com.decard.zj.founctiontest.R
import com.example.commonlibs.mvp.fragment.BaseCompatFragment
import kotlinx.android.synthetic.main.fragment_test.*


class TestFragment : BaseCompatFragment() {

    companion object {

        fun newInstance() =
                TestFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        btn_showCalendar.setOnClickListener {
            CalendarDialogFragment().show(fragmentManager!!, "calendar")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }
}
