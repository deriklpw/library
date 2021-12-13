package com.sky.library.singleton

import android.content.Context

class SingletonSyncKtLazy private constructor(context: Context) {

    companion object {
        @Volatile
        @JvmStatic
        private var instance: SingletonSyncKtLazy? = null

        @JvmStatic
        fun getInstance(context: Context) = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            instance = SingletonSyncKtLazy(context)
        }
    }
}