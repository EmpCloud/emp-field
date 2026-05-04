package com.empcloud.empmonitor.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File

object FileOperation {

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver

        // Handle different Uri schemes
        return when (uri.scheme) {
            "content" -> {
                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        return File(cursor.getString(columnIndex))
                    }
                }
                null
            }
            "file" -> {
                return File(uri.path)
            }
            else -> {
                null // Unsupported scheme
            }
        }
    }

}