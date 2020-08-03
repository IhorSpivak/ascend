package com.doneit.ascend.source.storage.remote.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.doneit.ascend.source.storage.remote.data.request.AttachmentRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

object MultipartConverter {
    fun MultipartBody.Builder.addAttachment(
        context: Context,
        attachment: AttachmentRequest,
        suffix: String = ""
    ) {
        val inputStream = context.contentResolver.openInputStream(
            Uri.parse(attachment.url)
        )!!
        val bytes = inputStream.use {
            it.readBytes()
        }
        val body = bytes.toRequestBody(
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

    fun getContentType(
        attachment: AttachmentRequest,
        context: Context
    ): String {
        return context.contentResolver.getType(Uri.parse(attachment.url)) ?: context.contentResolver.getType(
            FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                File(attachment.url)
            )
        ).toString()
    }
}