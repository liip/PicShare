package ch.liip.picshare.sharing.sharing.options

import android.os.Bundle

internal const val ImageUriKey = "$BaseKey.preview_image_uri_key"

class PreviewOptions : BaseOptions {

    private val titleKey = "$BaseKey.preview_title_key"
    var title: String
        get() = optionBundle.getString(titleKey, "")
        private set(title) = optionBundle.putString(titleKey, title)

    constructor() : super()
    internal constructor(bundle: Bundle) : super(bundle)

    fun setTitle(previewTitle: String): PreviewOptions {
        title = previewTitle
        return this
    }
}