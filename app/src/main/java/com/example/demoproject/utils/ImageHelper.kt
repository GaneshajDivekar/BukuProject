package com.example.demoproject.utils

import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


public object ImageHelper {
    var storageDir = File(Environment.getExternalStorageDirectory(), "SmartMart")

    fun timeStamp(uid: String): String {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString() + "_UID_" + uid
    }

    fun getOutputMediaFile(userId: String): File {
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File(
            storageDir.path +
                    File.separator + "IMG_" + timeStamp + "_UID_" + userId + ".jpg"
        )
    }
}