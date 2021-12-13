package com.sky.library.utils

import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.Exception
import java.nio.ByteBuffer
import java.util.ArrayList

/**
 * Created by derik on 17-2-28.
 */
object FileUtils {
    /**
     * 功能：遍历目录，获取文件
     *
     * @param dir       文件目录
     * @param listChild 是否遍历子目录
     * @return List 装有所有文件的集合
     */
    @JvmStatic
    fun getFiles(dir: File, listChild: Boolean): List<String?>? {
        if (!dir.isDirectory) {
            return null
        }
        var list: MutableList<String?>? = null
        val files = dir.listFiles()
        if (files != null && files.isNotEmpty()) {
            list = ArrayList()
            for (f in files) {
                if (f.isDirectory && listChild) {
                    getFiles(f, true)
                } else {
                    list.add(f.absolutePath)
                }
            }
        }
        return list
    }

    /**
     * 功能：单个文件比较
     *
     * @param localFile 本地文件
     * @param tarFile   目标文件
     * @return 正确，返回true；错误，返回false
     */
    @JvmStatic
    fun compare(localFile: File?, tarFile: File?): Boolean {
        val cmpFileMD5 = FileDigest.getFileMD5(localFile)
        val tarFileMD5 = FileDigest.getFileMD5(tarFile)
        return cmpFileMD5 != null && cmpFileMD5 == tarFileMD5
    }

    /**
     * 功能：指定路径下，建立文件
     *
     * @param path     存储目录的路径
     * @param fileName 存储文件名
     * @return File 新建的文件
     */

    @JvmStatic
    fun prepareFile(path: String, fileName: String): File? {

        // 外部存储, 先判断外部存储是否可用
        if (SDCardTest.sdcardState() == 1) {

            // 传入SD卡的路径
            val fileDir = File(path)
            if (!fileDir.exists()) {
                if (fileDir.mkdirs()) {
                    Log.i("ExternalPath", fileDir.absolutePath)
                } else {
                    Log.e("ExternalPath", "$path make failed!")
                }
            }
            val targetFile = File(
                fileDir.absolutePath + "/"
                        + fileName
            )
            try {
                if (targetFile.exists()) {
                    if (!targetFile.delete()) {
                        Log.e("File", "Delete failed:" + targetFile.absolutePath)
                    } else {
                        Log.i("File", "Deleted " + targetFile.absolutePath)
                    }
                }
                if (targetFile.createNewFile()) {
                    Log.i("File", "Created" + targetFile.absolutePath)
                } else {
                    Log.e("File", "Create failed:" + targetFile.absolutePath)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("File", "Create failed:" + targetFile.absolutePath)
            }
            return targetFile
        }
        return null
    }

    @JvmStatic
    fun createDir(path: String): File? {
        val dir = File(path)
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                return dir
            }
        } else {
            return dir
        }
        return null
    }

    @JvmStatic
    fun createFile(path: String, name: String, deleteIfExist: Boolean = true): File? {
        val dir = createDir(path)
        if (dir != null) {
            val file = File(dir, name)
            if (file.exists()) {
                if (deleteIfExist) {
                    if (file.delete() && file.createNewFile()) {
                        return file
                    }
                } else {
                    return file
                }
            } else if (file.createNewFile()) {
                return file
            }
        }
        return null
    }

    @JvmStatic
    fun writeStringToFile(targetFile: File, msg: String) {
        if (targetFile.exists()) {
            try {
                val rf = RandomAccessFile(targetFile, "rw")
                val channel = rf.channel
                val position = rf.length()
                rf.seek(position)
                channel.write(ByteBuffer.wrap(msg.toByteArray()))
                channel.close()
                rf.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}