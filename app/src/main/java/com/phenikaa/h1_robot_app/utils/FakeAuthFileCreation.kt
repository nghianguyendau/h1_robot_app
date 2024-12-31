package com.phenikaa.h1_robot_app.utils

import android.os.Environment
import java.io.File
import java.io.IOException

fun fakeAuthFileCreation() {
    try {
        // Đường dẫn file
        val path = Environment.getExternalStorageDirectory().absolutePath + File.separator + ".csj" + File.separator + ".csj.csj"
        val file = File(path)
        val parentFile = file.parentFile

        // Tạo thư mục nếu chưa tồn tại
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }

        // Tạo file nếu chưa tồn tại
        if (!file.exists()) {
            file.createNewFile()
        }

        // Nếu muốn, bạn có thể ghi thêm nội dung vào file
        file.writeText("Fake authentication data")

        println("Auth file created successfully at: $path")
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
