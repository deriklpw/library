package com.sky.library.singleton

import android.content.Context

class SingletonSyncKtLazy private constructor(context: Context) {

    companion object {

        @JvmStatic
        fun getInstance(context: Context) = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SingletonSyncKtLazy(context)
        }
    }
}