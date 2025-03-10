package com.phenikaa.h1_robot_app.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object TimeUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTimeISO8601(): String {
        val currentTime = ZonedDateTime.now(java.time.ZoneOffset.UTC)
        return currentTime.format(DateTimeFormatter.ISO_INSTANT)
    }
}