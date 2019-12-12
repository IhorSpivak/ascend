package com.doneit.ascend.presentation.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

private const val JPG = "jpg"

suspend fun Context.copyCompressed(sourcePath: String, destinationPath: String): String{
    val bitmap = BitmapFactory.decodeFile(sourcePath)
    val image = File(destinationPath)
    try{
        if(image.exists().not()) {
            image.createNewFile()
        }

        val out = FileOutputStream(image)

        if(sourcePath.contains(JPG)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, out)
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, out)
        }

        out.flush()
        out.close()
    } catch (e: Exception){ }

    return image.path
}

fun Context.createCameraPhotoUri(name: String): Uri {
    var directory = File(externalCacheDir!!.path)
    if (!directory.exists()) {
        directory.mkdirs()
    }

    val imageFile = File.createTempFile(name, ".jpg", directory)
    if(imageFile.exists().not()) {
        imageFile.createNewFile()
    }

    return Uri.fromFile(imageFile)
}

fun Activity.uriToFilePath(uri: Uri): String {
    val filePathColumn = MediaStore.Images.Media.DATA
    val c = contentResolver.query(uri , arrayOf(filePathColumn), null, null, null)
        ?: return ""
    c.moveToFirst()
    val index = c.getColumnIndex(filePathColumn)
    val path = c.getString(index)
    c.close()

    return path
}

