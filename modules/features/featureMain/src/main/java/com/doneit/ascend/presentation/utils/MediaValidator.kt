package com.doneit.ascend.presentation.utils

import android.content.Context
import android.net.Uri

object MediaValidator {

    private val supportedFormats = arrayOf(
        "pdf",
        "txt",
        "doc",
        "docx",
        "ppt",
        "pptx",
        "mp3",
        "mp4",
        "mov",
        "png",
        "jpg",
        "jpeg",
        "heif"
    )

    fun isUriSupported(context: Context, uri: Uri): Boolean {
        val type = context.contentResolver.getType(uri)
            ?.substringAfter('/') ?: return false
        return supportedFormats.contains(type)
    }
}