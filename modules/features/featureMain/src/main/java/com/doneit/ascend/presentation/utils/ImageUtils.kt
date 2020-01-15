package com.doneit.ascend.presentation.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val JPG = "JPEG"

suspend fun Activity.copyCompressed(source: Uri, destinationPath: String): String {
    var input = contentResolver.openInputStream(source)
    var bitmap = BitmapFactory.decodeStream(input)
    val image = File(destinationPath)

    try {
        if (image.exists().not()) {
            image.createNewFile()
        }

        //need to copy entire file due to android 10 external storage policy
        FileOutputStream(image).use { out ->
            val buf = ByteArray(1024)
            var len: Int
            len = input!!.read(buf)
            while (len > 0) {
                out.write(buf, 0, len)
                len = input.read(buf)
            }
        }
        //todo any better way to get path?
        val sourcePath = externalCacheDir!!.path + '/' + source.path!!.substring(source.path!!.lastIndexOf('/') + 1, source.path!!.length)
        /*todo uncomment this correct flow
           if(File(sourcePath).exists()) {
            copyEXIF(sourcePath, destinationPath)
        }*/

        FileOutputStream(image).use { out ->
            if(File(sourcePath).exists()){//todo comment this due to correct flow
                bitmap = bitmap.rotateImageIfRequired(sourcePath)
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, out)
        }

    } catch (e: IOException) {
        e.printStackTrace()
    }

    return image.path
}

private fun copyEXIF(sourcePath: String, destinationPath: String) {
    val oldExif = ExifInterface(sourcePath)
    val exifOrientation = oldExif.getAttribute(ExifInterface.TAG_ORIENTATION)

    if (exifOrientation != null) {
        val newExif = ExifInterface(destinationPath)
        newExif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation)
        newExif.saveAttributes()
    }
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

    val imageFile = File.createTempFile(name, ".JPEG", directory)
    if (imageFile.exists().not()) {
        imageFile.createNewFile()
    }

    return FileProvider.getUriForFile(this, "com.doneit.ascend.fileprovider", imageFile)
}

fun Context.createCropPhotoUri(name: String): Uri {
    return Uri.fromFile(
        File(
            this.cacheDir,
            name
        )
    )
}

fun Activity.cameraPhotoUri(name: String): Uri {
    var directory = File(externalCacheDir!!.path)
    if (!directory.exists()) {
        directory.mkdirs()
    }

    val imageFile = File.createTempFile(name, ".jpg", directory)
    if (imageFile.exists().not()) {
        imageFile.createNewFile()
    }

    return FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", imageFile)
}

fun Activity.uriToFilePath(uri: Uri): String {
    val filePathColumn = MediaStore.Images.Media.DATA
    val c = contentResolver.query(uri, arrayOf(filePathColumn), null, null, null)
        ?: return ""
    c.moveToFirst()
    val index = c.getColumnIndex(filePathColumn)
    val path = c.getString(index)
    c.close()

    return path
}

