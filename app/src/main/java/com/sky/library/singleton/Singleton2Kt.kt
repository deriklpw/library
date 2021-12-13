package com.sky.library.singleton

import android.content.Context

class Singleton2Kt private constructor(context: Context) {

    companion object {
        @JvmStatic
        private var INSTANCE: Singleton2Kt? = null

        @JvmStatic
        fun getInstance(context: Context): Singleton2Kt {
            if (INSTANCE == null) {
                INSTANCE = Singleton2Kt(context)
            }
            return INSTANCE!!
        }
    }
}