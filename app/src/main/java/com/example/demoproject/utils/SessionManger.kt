package com.example.demoproject.utils

import android.content.Context
import android.content.SharedPreferences


public class SessionManger(context: Context, prefFileName: String) {

    private val mPrefs: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    init {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        editor = mPrefs.edit()
    }


    fun isFlash(): Boolean {
        return mPrefs.getBoolean(
            PREF_FLASH, false
        )
    }

    fun setFlash(flag: Boolean) {
        mPrefs.edit().putBoolean(PREF_FLASH, flag)
            .apply()
    }

    fun isFront(): Boolean {
        return mPrefs.getBoolean(
            PREF_FRONT,
            false
        )
    }


    fun setFront(flag: Boolean) {
        mPrefs.edit().putBoolean(PREF_FRONT, flag)
            .apply()
    }


    fun getApiConstant(): String {
        return mPrefs.getString(
            API_CONSTANT,
            ""
        )
    }


    fun setApiConstant(flag: String) {
        mPrefs.edit().putString(API_CONSTANT, flag)
            .apply()
    }


    companion object {
        const val PREF_FILE_NAME = "SmartMart.pref"

        private const val PREF_FLASH = "flash"
        private const val PREF_FRONT = "front"
        private const val API_CONSTANT = "api_constant"


    }
}


