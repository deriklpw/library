package com.sky.library.singleton

/**
 * 静态内部类
 */
class SingletonStaticKt private constructor() {
    companion object {
        @JvmStatic
        fun getInstance(): SingletonStaticKt {
            return Singleton4KtHolder.INSTANCE
        }
    }

    private object Singleton4KtHolder {
        val INSTANCE: SingletonStaticKt = SingletonStaticKt()
    }
}