package com.doneit.ascend.source.storage.remote.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.doneit.ascend.source.storage.remote.data.request.AttachmentRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

object MultipartConverter {
    fun MultipartBody.Builder.addAttachment(
        context: Context,
        attachment: AttachmentRequest,
        suffix: String = ""
    ) {
        val dir = File(context.cacheDir, "temp")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = createTempFile(
            directory = dir,
            prefix = "def_$suffix",
            suffix = getContentType(attachment, context).substringAfterLast("/")
        )
        context.contentResolver.openInputStream(Uri.parse(attachment.url))?.use {
            BufferedInputStream(it).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        val body = file.asRequestBody(
            contentType = attachment.contentType
                .toMediaTypeOrNull()
        )
        addPart(
            MultipartBody.Part.createFormData(
                "attachment${suffix}",
                UUID.randomUUID().toString() + "." +
                        getContentType(attachment, context)
                            .substringAfterLast("/"),
                body
            )
        )
    }

    fun clearTempDir(context: Context) {
        val dir = File(context.cacheDir, "temp")
        if (dir.exists()) {
            dir.delete()
        }
    }

    private fun getContentType(
        attachment: AttachmentRequest,
        context: Context
    ): String {
        return context.contentResolver.getType(Uri.parse(attachment.url))
            ?: context.contentResolver.getType(
                FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileprovider",
                    File(attachment.url)
                )
            ).toString()
    }
}