import android.graphics.Bitmap
import android.os.Environment
import android.util.Base64
import android.util.Log
import java.io.*

object FileUtils {

    private val TAG = "------FileUtils"


    /**
     * 写入到指定文件夹下的文件
     */
    fun writeFileToSDCard(dirName: String, filename: String, filecontent: String) {
        // 先获取系统默认的文档存放根目录
        val parent_path = Environment.getExternalStorageDirectory()

        // 可以建立一个子目录专门存放自己专属文件
        val dir = File(parent_path.absoluteFile, dirName)
        if (!dir.exists())
            dir.mkdir()
        val file = File(dir.absoluteFile, filename)
        // 创建这个文件，如果不存在
        if (!file.exists()) {
            file.createNewFile()
        }
        val fos = FileOutputStream(file, true)
        // 开始写入数据到这个文件。
        fos.write(filecontent.toByteArray())
    }

    /**
     * 删除文件夹和目录下的文件
     */
    fun deleteDirectory(path: String) {
        val filePath = if (!path.endsWith(File.separator)) {
            path + File.separator
        } else {
            path
        }
        val dirFile = File(filePath)
        if (dirFile.exists() && dirFile.isDirectory) {
            val listFiles = dirFile.listFiles()
            for (file in listFiles) {
                file.delete()
            }
            dirFile.delete()
        }
    }

    /**
     * 保存bitmap到SD卡
     *fileName  文件夹名称
     * @param bitName 保存的名字
     * @param mBitmap 图片对像
     * return 生成压缩图片后的图片路径
     */
    fun saveMyBitmap(fileName: String, bitName: String, mBitmap: Bitmap): String {
        var fOut: FileOutputStream? = null
        // 先获取系统默认的文档存放根目录
        val parentPath = Environment.getExternalStorageDirectory()

        // 可以建立一个子目录专门存放自己专属文件
        val dir = File(parentPath.absoluteFile, fileName)
        if (!dir.exists()) {
            dir.mkdir()
        }

        val photo = File(dir.absoluteFile, "$bitName.png")
        try {
            // 创建这个文件，如果不存在
            if (!photo.exists()) {
                photo.createNewFile()
            }
            fOut = FileOutputStream(photo)
            mBitmap.compress(Bitmap.CompressFormat.PNG, 10, fOut)
            fOut.flush()
            fOut.close()

        } catch (e: IOException) {
            return "在保存图片时出错：" + e.toString()
        }
        return photo.absolutePath
    }

    /**
     * 删除单个文件
     */
    fun deleteFile(str: String): Boolean {
        val file = File(str)
        if (file.isFile && file.exists()) {
            return file.delete()
        }
        return false
    }

    /**
     * 将文件转为base64字符串
     */
    fun fileToBase64(file: File): String? {
        var base64: String? = null
        var `in`: InputStream? = null
        try {
            `in` = FileInputStream(file)
            Log.d("-----", `in`.available().toString())
            val bytes = ByteArray(`in`.available())
            val length = `in`.read(bytes)
            Log.d("-----", length.toString())
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {

            e.printStackTrace()
        } finally {
            try {
                `in`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return base64
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    fun renameFile(oldPath: String, newPath: String) {
        val oleFile = File(oldPath)
        val newFile = File(newPath)
        //执行重命名
        oleFile.renameTo(newFile)
    }


}
