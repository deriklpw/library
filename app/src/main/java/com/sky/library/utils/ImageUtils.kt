package com.sky.library.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import java.lang.Exception
import kotlin.math.min

/**
 * description ：
 * author : Derik.Wu
 * email : Derik.Wu@waclighting.com.cn
 * date : 2020/7/30
 */

private const val COMPENSATION_VALUE = 20

object ImageUtils {

    @Suppress("DEPRECATION")
    fun setImageColor(image: ImageView, isOn: Boolean, level: Int, max: Int) {
        if (isOn) {
            setImageColor(image, level, max)
        } else {
            image.background?.apply {
                setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    @Suppress("DEPRECATION")
    fun setImageColor(image: ImageView, level: Int, max: Int) {
        try {
            image.background?.apply {
                var percent = ((255 - COMPENSATION_VALUE) / max.toFloat()) * min(level, max)
                percent += COMPENSATION_VALUE
                //"#ffffbb33"
                if (percent < 16) {
                    val color = "#0${Integer.toHexString(percent.toInt())}ffbb33"
                    setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
                } else {
                    val color = "#${Integer.toHexString(percent.toInt())}ffbb33"
                    setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}