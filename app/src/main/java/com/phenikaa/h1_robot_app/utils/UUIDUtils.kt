package com.phenikaa.h1_robot_app.utils

import java.util.UUID

object UUIDUtils {
    fun generateRandomUUID(): String {
        return UUID.randomUUID().toString()
    }
}