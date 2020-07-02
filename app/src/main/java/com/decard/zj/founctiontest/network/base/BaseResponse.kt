package com.decard.zj.founctiontest.network.base

/**
 * @Author: liuwei
 *
 * @Create: 2019/5/24 16:43
 *
 * @Description:

 */
data class BaseResponse<T>(
    val msg: String,
    val result: T,
    val status: Int

) {
    override fun toString(): String {
        return "BaseResponse(msg='$msg', result=$result, status=$status)"
    }
}

