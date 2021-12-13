package com.sky.library.singleton

import android.content.Context
import kotlin.jvm.Volatile

/**
 * 双重检测
 */
class SingletonSynckt private constructor(context: Context) {

    companion object {
        @Volatile
        @JvmStatic
        private var INSTANCE: SingletonSynckt? = null

        @JvmStatic
        fun getInstance(context: Context): SingletonSynckt {
            if (INSTANCE == null) {
                synchronized(SingletonSynckt::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = SingletonSynckt(context)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}