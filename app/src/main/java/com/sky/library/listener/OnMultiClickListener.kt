package com.sky.library.listener

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View

/**
 * description ：single click and double click event
 * author : Derik.Wu
 * email : Derik.Wu@waclighting.com.cn
 * date : 2020/12/22
 */

private const val SINGLE_CLICK_EVENT_TIME = 600
abstract class OnMultiClickListener : View.OnClickListener {
    //for double
    var mHints = longArrayOf(0, 0)
    var mHandler = Handler(Looper.getMainLooper())
    var isDoubleClick = false

    override fun onClick(v: View) {
        isDoubleClick = false
        val runnable = Runnable {
            if (!isDoubleClick) {
                onSingleClick(v)
                mHints.forEachIndexed { index, _ ->
                    mHints[index] = 0
                }
            }
        }
        System.arraycopy(mHints, 1, mHints, 0, mHints.size - 1)
        mHints[mHints.size - 1] = SystemClock.uptimeMillis()
        if (SystemClock.uptimeMillis() - mHints[0] <= SINGLE_CLICK_EVENT_TIME - 50) {
            mHandler.removeCallbacks(runnable)
            onDoubleClick(v)
            isDoubleClick = true
        } else {
            mHandler.removeCallbacks(runnable)
            mHandler.postDelayed(runnable, SINGLE_CLICK_EVENT_TIME.toLong())
        }
    }

    abstract fun onDoubleClick(v: View)

    abstract fun onSingleClick(v: View)
}