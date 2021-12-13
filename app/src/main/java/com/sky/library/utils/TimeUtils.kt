package com.sky.library.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    @JvmStatic
    fun getCurrentDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    @JvmStatic
    fun getCurrentDate2(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    @JvmStatic
    fun getCurrentTime(): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    @JvmStatic
    fun getCurrentDateTime(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    @JvmStatic
    fun getCurrentDateTime2(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    @JvmStatic
    fun timeToString(totalTimeSeconds: Long): String {
        val hour = totalTimeSeconds / 3600
        val left = totalTimeSeconds % 3600
        val minutes = left / 60
        val seconds = left % 60
        return String.format("%d时%d分%d秒", hour, minutes, seconds)
    }

    @JvmStatic
    fun timeToString2(totalTimeSeconds: Long): String {
        val hour = totalTimeSeconds / 3600
        val left = totalTimeSeconds % 3600
        val minutes = left / 60
        val seconds = left % 60
        return String.format("%d:%d:%d", hour, minutes, seconds)
    }
}