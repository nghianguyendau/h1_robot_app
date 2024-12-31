package com.phenikaa.h1_robot_app.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.os.Environment
import android.util.Log
import java.io.File

object SharedPreferencesSDCard {
    private const val TAG = "SharedPreferencesSDCard"
    private var mySharedPreferences: SharedPreferences? = null

    val sharedPreferences: SharedPreferences?
        get() = mySharedPreferences

    fun init(context: Context) {
        mySharedPreferences = getMySharedPreferences(
            context,
            Environment.getExternalStorageDirectory().absolutePath + "/csjbot_sdk_demo",
            "csjbot_sdk_demo"
        )
    }

    private fun getMySharedPreferences(
        context: Context,
        dir: String,
        fileName: String
    ): SharedPreferences {
        try {
            val field_mBase = ContextWrapper::class.java.getDeclaredField("mBase")
            field_mBase.isAccessible = true
            val obj_mBase = field_mBase[context]
            val field_mPreferencesDir = obj_mBase.javaClass.getDeclaredField("mPreferencesDir")
            field_mPreferencesDir.isAccessible = true
            val file = File(dir)
            field_mPreferencesDir[obj_mBase] = file
            Log.e(TAG, "getMySharedPreferences filep=${file.absolutePath} | fileName=$fileName")
            return context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e(TAG, "getMySharedPreferences end filename=$fileName")
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }
}
