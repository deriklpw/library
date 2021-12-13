package com.sky.library.utils

import android.content.Context
import android.content.SharedPreferences

object PrefUtils {
    // The name of sp, strut_config
    private const val BASE_CONFIG = "base_config"

    @JvmStatic
    fun readString(context: Context, key: String): String? {
        return getSharedPreferencesWithContext(context).getString(key, "")
    }

    @JvmStatic
    fun readString(context: Context, key: String, def: String?): String? {
        return getSharedPreferencesWithContext(context).getString(key, def)
    }

    @JvmStatic
    fun writeString(context: Context, key: String, value: String?) {
        getSharedPreferencesWithContext(context).edit().putString(key, value).apply()
    }

    @JvmStatic
    fun readBoolean(context: Context, key: String?): Boolean {
        return getSharedPreferencesWithContext(context).getBoolean(key, false)
    }

    @JvmStatic
    fun readBoolean(context: Context, key: String?, defValue: Boolean): Boolean {
        return getSharedPreferencesWithContext(context).getBoolean(key, defValue)
    }

    @JvmStatic
    fun writeBoolean(context: Context, key: String?, value: Boolean) {
        getSharedPreferencesWithContext(context).edit().putBoolean(key, value).apply()
    }

    @JvmStatic
    fun readInt(context: Context, key: String?): Int {
        return getSharedPreferencesWithContext(context).getInt(key, 0)
    }

    @JvmStatic
    fun readInt(context: Context, key: String?, defValue: Int): Int {
        return getSharedPreferencesWithContext(context).getInt(key, defValue)
    }

    @JvmStatic
    fun writeInt(context: Context, key: String?, value: Int) {
        getSharedPreferencesWithContext(context).edit().putInt(key, value).apply()
    }

    @JvmStatic
    fun readLong(context: Context, key: String?): Long {
        return getSharedPreferencesWithContext(context).getLong(key, 0)
    }

    @JvmStatic
    fun writeLong(context: Context, key: String?, value: Long) {
        getSharedPreferencesWithContext(context).edit().putLong(key, value).apply()
    }

    @JvmStatic
    fun remove(context: Context, key: String?) {
        getSharedPreferencesWithContext(context).edit().remove(key).apply()
    }

    @JvmStatic
    fun removeAll(context: Context) {
        getSharedPreferencesWithContext(context).edit().clear().apply()
    }

    private fun getSharedPreferencesWithContext(context: Context): SharedPreferences {
        return context.getSharedPreferences(BASE_CONFIG, Context.MODE_PRIVATE)
    }
}