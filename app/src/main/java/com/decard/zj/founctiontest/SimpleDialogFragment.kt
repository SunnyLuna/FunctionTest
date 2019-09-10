package com.decard.zj.founctiontest

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment


class SimpleDialogFragment : DialogFragment(), DialogInterface.OnClickListener {


    override fun onClick(dialog: DialogInterface?, which: Int) {

    }

    /**
     * 方式11111111
     * 自定义view
     */
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_simple_dialog, container)
//        view.tv_dialog.setOnClickListener {
//            Toast.makeText(context, "点击", Toast.LENGTH_LONG).show()
//        }
//        return view
//
//    }


    /**
     * 方式二
     * 原版
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return AlertDialog.Builder(activity)
                .setTitle("诗经")
                .setMessage("蒹葭")
                .setPositiveButton("确认", this)
                .setNegativeButton("取消", this)
                .show()
    }


}
