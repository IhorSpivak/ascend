package com.doneit.ascend.presentation.video_chat.attachments.common

import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.net.toUri
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import java.io.File

abstract class AttachmentViewHolder(
    view: View
) : LifecycleViewHolder(view) {
    protected fun isFileExist(filename: String): Boolean {
        val dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toUri()
        return File("${dir.path}", filename).exists()
    }
}