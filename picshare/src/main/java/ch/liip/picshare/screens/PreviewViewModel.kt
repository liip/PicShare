package ch.liip.picshare.screens

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.liip.picshare.helpers.load
import ch.liip.picshare.sharing.sharing.options.PreviewOptions

internal class PreviewViewModel : ViewModel() {

    var previewUriString: String = ""
        private set

    private var _previewBitmap = MutableLiveData<Bitmap>()
    private var _previewTitle = MutableLiveData<String>()
    private var _sharemage = MutableLiveData<Unit>()

    val previewBitmap: LiveData<Bitmap> = _previewBitmap
    val previewTitle: LiveData<String> = _previewTitle
    val shareImage: LiveData<Unit> = _sharemage

    fun reloadFrom(options: PreviewOptions, uriString: String, contentResolver: ContentResolver) {

        if (previewUriString == "" || previewUriString != uriString) {
            previewUriString = uriString
            _previewBitmap.value = load(contentResolver, uriString, false)
        }

        _previewTitle.value = options.title
    }

    fun shareButtonClicked() {
        _sharemage.value = Unit
    }
}