package com.doneit.ascend.presentation.utils

import android.content.Context
import android.net.Uri

object MediaValidator {

    val supportedFormats = arrayOf(
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

    const val allowedSizeInBytes = 1000000 * 100

    inline fun executeIfUriSupported(
        context: Context,
        uri: Uri,
        onError: (ValidationError) -> Unit,
        action: () -> Unit
    ) {
        val type = context.contentResolver.getType(uri)
            ?.substringAfter('/') ?: return
        context.contentResolver.openInputStream(uri)?.use {
            when {
                !supportedFormats.contains(type) -> onError(ValidationError.FORMAT)
                it.available() >= allowedSizeInBytes -> onError(ValidationError.SIZE)
                else -> action()
            }
        }
    }

    enum class ValidationError {
        SIZE,
        FORMAT
    }
}