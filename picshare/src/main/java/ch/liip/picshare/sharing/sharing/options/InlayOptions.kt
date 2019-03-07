package ch.liip.picshare.sharing.sharing.options

import android.os.Bundle
import ch.liip.picshare.inlaying.InlayViewProvider

class InlayOptions : BaseOptions {

    private val inlayViewProviderKey = "$BaseKey.inlay_view_provider"
    var inlayViewProvider: InlayViewProvider
        get() = optionBundle.getSerializable(inlayViewProviderKey) as InlayViewProvider
        private set(provider) = optionBundle.putSerializable(inlayViewProviderKey, provider)

    constructor(viewProvider: InlayViewProvider) : super() {
        inlayViewProvider = viewProvider
    }

    internal constructor(bundle: Bundle) : super(bundle)
}