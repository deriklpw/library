package com.derik.library.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

/**
 * description ：
 * author : Derik.Wu
 * email : Derik.Wu@waclighting.com.cn
 * date : 2020/7/14
 */
object ActivityUtils {

    @JvmStatic
    fun addFragment(activity: FragmentActivity, frameId: Int, fragment: Fragment, addToStack: Boolean, tag: String, allowLoss: Boolean) {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        if (addToStack) {
            transaction.addToBackStack(tag)
        }
        transaction.add(frameId, fragment, tag)
        if (allowLoss) transaction.commitAllowingStateLoss() else transaction.commit()
    }

    @JvmStatic
    fun replaceFragment(activity: FragmentActivity, frameId: Int, fragment: Fragment, addToStack: Boolean, tag: String) {
        replaceFragment(activity, frameId, fragment, addToStack, tag, true)
    }

    @JvmStatic
    fun replaceFragment(activity: FragmentActivity, frameId: Int, fragment: Fragment, addToStack: Boolean, tag: String, allowLoss: Boolean) {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        if (addToStack) {
            transaction.addToBackStack(tag)
        }
        transaction.replace(frameId, fragment, tag)
        if (allowLoss) transaction.commitAllowingStateLoss() else transaction.commit()
    }

    @JvmStatic
    fun remove(activity: FragmentActivity, fragment: Fragment) {
        remove(activity, fragment, true)
    }

    @JvmStatic
    fun remove(activity: FragmentActivity, fragment: Fragment, allowLoss: Boolean) {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        transaction.remove(fragment)
        if (allowLoss) transaction.commitAllowingStateLoss() else transaction.commit()
    }

}