package com.doneit.ascend.source.storage.remote.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
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
        val uri = Uri.parse(attachment.url)
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = createTempFile(
            directory = dir,
            prefix = "def_$suffix",
            suffix = getContentType(attachment, context).substringAfterLast("/")
        )
        context.contentResolver.openInputStream(uri)?.use {
            BufferedInputStream(it).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        val body = file.asRequestBody(
            contentType = attachment.contentType.replace(
                "*", getContentType(attachment, context)
                    .substringAfterLast("/")
            )
                .toMediaTypeOrNull()
        )
        addPart(
            MultipartBody.Part.createFormData(
                "attachment${suffix}",
                context.contentResolver.getFilenameAndSizeFromUri(uri).first,
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

    fun ContentResolver.getFilenameAndSizeFromUri(uri: Uri): Pair<String, Long> {
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.SIZE)
        query(uri, projection, null, null, null)?.use {
            if (it.moveToFirst()) {
                return it.getString(0) to it.getLong(1)
            }
        }
        return UUID.randomUUID().toString() to 0
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