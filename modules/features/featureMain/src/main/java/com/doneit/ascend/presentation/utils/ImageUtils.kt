package com.doneit.ascend.presentation.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val JPG = "jpg"

suspend fun copyCompressed(sourcePath: String, destinationPath: String): String{
    val bitmap = BitmapFactory.decodeFile(sourcePath).rotateImageIfRequired(sourcePath)
    val image = File(destinationPath)
    try{
        if(image.exists().not()) {
            image.createNewFile()
        }

        FileOutputStream(image).use {out ->
            if(sourcePath.contains(JPG)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, out)
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, out)
            }
        }

    } catch (e: Exception){ }

    return image.path
}

private fun Bitmap.rotateImageIfRequired(selectedImage: String): Bitmap {

    val ei = ExifInterface(selectedImage)
    val orientation =
        ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270)
        else -> this
    }
}

private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
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

