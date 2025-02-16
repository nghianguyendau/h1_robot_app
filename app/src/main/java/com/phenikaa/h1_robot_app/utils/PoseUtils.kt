package com.phenikaa.h1_robot_app.utils

import com.phenikaa.h1_robot_app.data.model.Point
import org.json.JSONObject

object NavigationUtils {
    data class NavigationPoint(
        val x: Double,
        val y: Double,
        val z: Double = 0.0,
        val rotation: Double
    )

    /**
     * Chuyển đổi từ một Point sang chuỗi JSON navigation
     */
    fun Point.toNavigationString(): String {
        return createNavigationString(
            x = x.toDouble(),
            y = y.toDouble(),
            z = z?.toDouble() ?: 0.0,
            rotation = rotation.toDouble()
        )
    }

    /**
     * Chuyển đổi từ JSONObject pose sang chuỗi JSON navigation
     */
    fun JSONObject.toNavigationString(): String {
        return createNavigationString(
            x = getDouble("x"),
            y = getDouble("y"),
            z = optDouble("z", 0.0),
            rotation = getDouble("rotation")
        )
    }

    /**
     * Chuyển đổi nhiều Points sang list chuỗi JSON navigation
     */
    fun List<Point>.toNavigationStrings(): List<String> {
        return map { it.toNavigationString() }
    }

    fun Set<Point>.toNavigationStrings(): List<String> {
        return toList().toNavigationStrings()
    }

    /**
     * Hàm helper để tạo chuỗi JSON navigation
     */
    private fun createNavigationString(
        x: Double,
        y: Double,
        z: Double,
        rotation: Double
    ): String {
        return """{"x": $x, "y": $y, "z": "$z", "rotation": $rotation}"""
    }
}