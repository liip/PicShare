package ch.liip.picshare.helpers

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import java.io.File

private const val SHARED_FOLDER = "picshareprovider"
private const val SHARED_IMAGE_NAME = "share.png"


/**
 * This returns a Uri of the form content://fileprovider.accessible.path
 * It points to the same file as the localUri passed but can be used for sharing outside the app
 * local FileOutputStreams are not able to write using content:// uri, so localImageUri should be used instead
 */
internal fun sharableUri(context: Context, localUri: Uri) = FileProvider.getUriForFile(context, "${context.packageName}.ch.liip.picshare.provider", localUri.toFile())

/**
 * This returns a Uri of the form file://path.to.localfile
 * It points to the same file as the sharableImageUri function but can be used inside this app to write to the file
 * Others app won't be able to read file:// uri, so for sharing, sharableImageUri should be used instead
 */
internal fun localImageUri(context: Context): Uri = Uri.fromFile(sharedImageFile(context))

private fun sharedImageFile(context: Context): File = File("${context.cacheDir}/$SHARED_FOLDER", SHARED_IMAGE_NAME)

internal fun createEmptyCacheFile(context: Context) {
    val file = sharedImageFile(context)

    if(!file.exists()) {
        file.parentFile.mkdirs()
        file.createNewFile()
    }
}

internal fun destroyCacheFile(context: Context) {
    val file = sharedImageFile(context)

    if(file.exists()) {
        file.delete()
    }
}