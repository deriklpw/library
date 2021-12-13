package com.derik.library.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun getCurrentDay(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    fun getCurrentTime(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    fun getCurrentTimeForLogName(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }
}