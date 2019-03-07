package ch.liip.picshare.sharing.sharing.options

import android.os.Bundle

class SharePanelOptions : BaseOptions {

    private val shareTitleKey = "$BaseKey.share_title_key"
    var shareTitle: String
        get() = optionBundle.getString(shareTitleKey, "")
        private set(value) = optionBundle.putString(shareTitleKey, value)

    private val shareImageKey = "$BaseKey.image_title_key"
    var imageTitle: String
        get() = optionBundle.getString(shareImageKey, "")
        private set(value) = optionBundle.putString(shareImageKey, value)

    constructor(title: String, imageName: String) : super() {
        shareTitle = title
        imageTitle = imageName
    }
    internal constructor(bundle: Bundle) : super(bundle)
}