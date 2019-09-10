package com.decard.zj.founctiontest.download

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults


/**
 *
 * @author ZJ
 * created at 2019/8/5 14:56
 */
object DBUtil {

    /**
     * 增
     */
    fun addData(dataBean: DataBean) {
        Log.d("---------db", dataBean.toString())
        Realm.getDefaultInstance().executeTransaction {
            it.copyToRealmOrUpdate(dataBean)
        }
    }

    /**
     * 删
     */
    fun deleteData(dataBean: DataBean) {
        Realm.getDefaultInstance().executeTransaction {
            it.delete(dataBean.javaClass)
        }
    }

    /**
     * 根据taskId删除
     */
    fun deleteByTaskId(taskId: String) {
        //先查找到数据
        val userList = Realm.getDefaultInstance().where(DataBean::class.java).equalTo("taskId", taskId).findAll()
        Realm.getDefaultInstance().executeTransaction {
            userList.get(0)!!.deleteFromRealm()
        }
    }

    /**
     * 改
     */
    fun updateData(taskId: String, progress: Int, status: String) {
        Realm.getDefaultInstance().executeTransaction {
            //先查找后得到User对象
            val user = Realm.getDefaultInstance().where(DataBean::class.java).equalTo("taskId", taskId).findFirst()
            user!!.progress = progress
            user.status = status
        }
    }


    /**
     * 查
     */
    fun queryAll(): List<DataBean> {
        val beans: RealmResults<DataBean> = Realm.getDefaultInstance().where(DataBean::class.java).findAll()
        return Realm.getDefaultInstance().copyFromRealm(beans)
    }


    /**
     * 根据taskID查
     */
    fun queryByTaskId(taskId: String): DataBean? {
        val findFirst = Realm.getDefaultInstance().where(DataBean::class.java).equalTo("taskId", taskId).findFirst()
        if (findFirst != null) {

            return Realm.getDefaultInstance().copyFromRealm(findFirst)
        }
        return null
    }

    fun close() {
        Realm.getDefaultInstance().close()
    }

}
