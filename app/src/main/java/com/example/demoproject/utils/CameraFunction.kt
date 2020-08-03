package com.example.demoproject.utils

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel


public fun  getImagePath(uri: Uri?,activity:Activity): String? {
    var cursor =
        activity!!.contentResolver.query(uri!!, null, null, null, null)
    cursor!!.moveToFirst()
    var document_id = cursor.getString(0)
    document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
    cursor.close()
    cursor = activity!!.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null,
        MediaStore.Images.Media._ID + " = ? ",
        arrayOf(document_id),
        null
    )
    cursor!!.moveToFirst()
    val path =
        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
    cursor.close()
    return path
}

public fun copyFileOrDirectory(
    srcDir: String?,
    dstDir: String?,
    str: String?
) {
    try {
        val src = File(srcDir)
        val dst = File(dstDir, str)
        if (src.isDirectory) {
            val files = src.list()
            val filesLength = files.size
            for (i in 0 until filesLength) {
                val src1 = File(src, files[i]).path
                val dst1 = dst.path
                copyFileOrDirectory(str, src1, dst1)
            }
        } else {
            copyFile(src, dst)
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

@Throws(IOException::class)
public fun copyFile(sourceFile: File?, destFile: File) {
    if (!destFile.parentFile.exists()) destFile.parentFile.mkdirs()
    if (!destFile.exists()) {
        destFile.createNewFile()
    }
    var source: FileChannel? = null
    var destination: FileChannel? = null
    try {
        source = FileInputStream(sourceFile).channel
        destination = FileOutputStream(destFile).channel
        destination.transferFrom(source, 0, source.size())
    } finally {
        source?.close()
        destination?.close()
    }
}


