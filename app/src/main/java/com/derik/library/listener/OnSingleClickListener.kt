package com.derik.library.listener

import android.view.View

/**
 * author : Derik.Wu
 * email : Derik.Wu@waclighting.com.cn
 * date : 2020/4/21
 */

abstract class OnSingleClickListener : View.OnClickListener {
    private var lastClickTime: Long = 0
    override fun onClick(v: View) {
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime >= 600) {
            onSingleClick(v)
        }
        lastClickTime = currentClickTime
    }

    abstract fun onSingleClick(v: View)
}