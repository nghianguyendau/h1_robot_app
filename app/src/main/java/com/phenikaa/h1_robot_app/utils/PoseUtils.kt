package com.phenikaa.h1_robot_app.utils

import com.phenikaa.h1_robot_app.data.model.Point
import org.json.JSONObject

object NavigationUtils {
    /**
     * Chuyển đổi từ Point sang string JSON navigation với format chuẩn
     */
    fun Point.toNavigationString(): String {
        return """{"x": $x, "y": $y, "z": "0.0", "rotation": $rotation}"""
    }

    /**
     * Chuyển đổi List<Point> sang List<String> JSON
     */
    fun List<Point>.toNavigationStrings(): List<String> {
        return map { it.toNavigationString() }
    }

    /**
     * Chuyển đổi Set<Point> sang List<String> JSON
     */
    fun Set<Point>.toNavigationStrings(): List<String> {
        return toList().toNavigationStrings()
    }

    /**
     * Chuyển đổi từ JSONObject pose sang string navigation
     */
    fun JSONObject.toNavigationString(): String {
        return """{"x": ${getDouble("x")}, "y": ${getDouble("y")}, "z": "0.0", "rotation": ${getDouble("rotation")}}"""
    }
}