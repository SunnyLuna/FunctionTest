package com.decard.zj.founctiontest.download

/**
 * @Author: liuwei
 *
 * @Create: 2019/5/28 17:19
 *
 * @Description:

 */

data class AllAppInfoResponse(
    val compatible_device: String,
    val file_md5: String,
    val icon_id: String,
    val icon_url: String,
    val software_download_count: String,
    val software_id: String,
    val software_name: String,
    val software_package_name: String,
    val software_size: String,
    val software_sketch: String,
    val software_version: String,
    val timestamp: String
)

