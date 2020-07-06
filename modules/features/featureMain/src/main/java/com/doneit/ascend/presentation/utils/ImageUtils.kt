package com.doneit.ascend.presentation.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.doneit.ascend.presentation.utils.extensions.toTimeStampFormat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

const val TEMP_IMAGE_NAME = "temp.jpeg"

fun Context.copyToStorage(bitmap: Bitmap): String{
    return try {
        val imageFile = this.createImageFile()
        val outputStream = FileOutputStream(imageFile)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, outputStream)
            imageFile.absolutePath
        }finally {
            outputStream.flush()
            outputStream.close()
        }
    }catch (e: IOException){
        ""
    }
}

fun Activity.checkImage(path: String): String{
    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
        File.createTempFile(
            "JPEG_${Date().toTimeStampFormat()}_",
            ".jpg",
            it /* directory */
        ).apply {
            val bitmap = BitmapFactory.decodeFile(path).rotateImageIfRequired(path)
            FileOutputStream(absolutePath).also {
                bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, it)
            }
            return  absolutePath
        }
    }
    return ""
}

fun Context.copyToStorage(uri: Uri): String{
        contentResolver.openInputStream(uri).use { inputStream ->
            BitmapFactory.decodeStream(inputStream).let { bitmap ->
                val imageFile = this.createImageFile()
                val outputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        Constants.COMPRESSION_QUALITY,
                        outputStream
                    )
                    return imageFile.absolutePath
                } finally {
                    outputStream.flush()
                    outputStream.close()
                }
            }
        }
}
suspend fun Activity.copyCompressed(source: Uri, destinationPath: String): String {
    var input = contentResolver.openInputStream(source)
    var bitmap = BitmapFactory.decodeStream(input)
    val outFile = File(destinationPath)

    try {
        if (outFile.exists().not()) {
            outFile.createNewFile()
        }

        /* todo uncomment this correct flow
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

           if(File(sourcePath).exists()) {
            copyEXIF(sourcePath, destinationPath)
        }
        */
        //todo any better way to get path?
        val sourcePath = externalCacheDir!!.path + '/' + source.path!!.substring(source.path!!.lastIndexOf('/') + 1, source.path!!.length)

        FileOutputStream(outFile).use { out ->
            if(File(sourcePath).exists()){//todo comment this due to correct flow
                bitmap = bitmap.rotateImageIfRequired(sourcePath)
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, out)
        }

        input?.close()

    } catch (e: IOException) {
        e.printStackTrace()
    }

    return outFile.path
}

/*fun Context.copyImageFromSource(source: Uri): String{
    return try {
        contentResolver.openInputStream(source).let {inputStream ->
            BitmapFactory.decodeStream(inputStream).let {bitmap ->
                val imageFile = this.createImageFile()
                val outputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, outputStream)
                    bitmap.rotateImageIfRequired(imageFile.absolutePath).let {
                        it.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, outputStream)
                        imageFile.absolutePath
                    }
                }finally {
                    inputStream?.close()
                    outputStream.apply {
                        flush()
                        close()
                    }
                }
            }
        }
    }catch (e: IOException){
        ""
    }
}*/

fun Context.copyFile(source: Uri, destination: Uri): Uri {
    val input = contentResolver.openInputStream(source)
    contentResolver.openOutputStream(destination)?.use { out ->
        val buf = ByteArray(1024)
        var len: Int
        len = input!!.read(buf)
        while (len > 0) {
            out.write(buf, 0, len)
            len = input.read(buf)
        }
    }
    input?.close()

    return destination
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
    val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

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
    return Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
}

fun Context.createTempPhotoUri(): Uri {
    return createTempFile(TEMP_IMAGE_NAME)
}

fun Context.createCropPhotoUri(): Uri {
    return Uri.fromFile(
        File(
            this.externalCacheDir,
            "temp_crop.jpeg"
        )
    )
}

fun Context.getCompressedImagePath(): String {
    return externalCacheDir!!.path + File.separatorChar + "temp_compressed.jpeg"
}

private fun Context.createTempFile(name: String): Uri {
    var directory = File(externalCacheDir!!.path)
    if (!directory.exists()) {
        directory.mkdirs()
    }

    val fullPath = directory.path + File.separatorChar + name
    val imageFile = File(fullPath)
    if (imageFile.exists().not()) {
        imageFile.createNewFile()
    }

    return FileProvider.getUriForFile(this, "com.doneit.ascend.fileprovider", imageFile)
}

@Throws(IOException::class)
private fun Context.createImageFile(): File {
    // Create an image file name
    val storageDir: File = File(externalCacheDir!!.path)
    return File.createTempFile(
        "JPEG_${Date().toTimeStampFormat()}_",
        ".jpg",
        storageDir
    )
}
fun Activity.getImagePath(uri: Uri): String{
    contentResolver.query(uri, null, null, null, null).use { cursor ->
        cursor?.moveToFirst()
        return cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)).orEmpty()
    }
}