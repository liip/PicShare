package ch.liip.picshare.helpers

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

internal fun save(bitmap: Bitmap, uri: Uri): Boolean {
    val file = File(uri.path)
    return save(bitmap, file)
}

private fun save(bitmap: Bitmap, file: File): Boolean {
    try {
        val outputStream = FileOutputStream(file.absoluteFile)

        val stream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

        val byteArray = stream.toByteArray()

        outputStream.write(byteArray)

        outputStream.close()

        return true
    } catch (exception: IOException) {
        exception.printStackTrace()
        return false
    }

}


internal fun load(contentResolver: ContentResolver, sourceUriString: String, mutable: Boolean): Bitmap {
    return load(contentResolver, Uri.parse(sourceUriString), mutable)
}

internal fun load(contentResolver: ContentResolver, sourceUri: Uri, mutable: Boolean): Bitmap {
    val selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver, sourceUri)
    val bitmap = selectedBitmap.copy(Bitmap.Config.ARGB_8888, mutable)
    selectedBitmap.recycle()
    return bitmap
}