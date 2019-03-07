package ch.liip.picshare.sharing.sharing.options

import android.os.Bundle

internal class SharingOptions : BaseOptions {

    private val cropKey = "$BaseKey.crop_options"

    private val inlayKey = "$BaseKey.inlay_options"

    private val previewKey = "$BaseKey.preview_options"

    private val sharePanelOptionsKey = "$BaseKey.share_panel_options"

    private val selectionOptionsKey = "$BaseKey.selection_options"

    val isCropEnabled = optionBundle.containsKey(cropKey)

    val isInlayEnabled = optionBundle.containsKey(inlayKey)

    val isPreviewEnabled = optionBundle.containsKey(previewKey)


    constructor(selectionOptions: SelectionOptions, sharePanelOptions: SharePanelOptions) : super() {
        setSelectionOptions(selectionOptions)
        setSharePanelOptions(sharePanelOptions)
    }
    constructor(bundle: Bundle) : super(bundle)

    private fun setSelectionOptions(options: SelectionOptions) {
        optionBundle.putBundle(selectionOptionsKey, options.bundle())
    }

    private fun setSharePanelOptions(options: SharePanelOptions) {
        optionBundle.putBundle(sharePanelOptionsKey, options.bundle())
    }

    fun setCrop(options: CropOptions?): SharingOptions {
        options?.let {optionBundle.putBundle(cropKey, options.bundle())}
        return this
    }

    fun setInlay(options: InlayOptions?): SharingOptions  {
        options?.let {optionBundle.putBundle(inlayKey, options.bundle())}
        return this
    }

    fun setPreview(options: PreviewOptions?): SharingOptions  {
        options?.let {optionBundle.putBundle(previewKey, options.bundle())}
        return this
    }

    fun cropOptions(): CropOptions = CropOptions(optionBundle.getBundle(cropKey)!!)

    fun inlayOptions(): InlayOptions = InlayOptions(optionBundle.getBundle(inlayKey)!!)

    fun previewOptions(): PreviewOptions = PreviewOptions(optionBundle.getBundle(previewKey)!!)

    fun selectionOptions(): SelectionOptions = SelectionOptions(optionBundle.getBundle(selectionOptionsKey)!!)

    fun sharePanelOptions(): SharePanelOptions = SharePanelOptions(optionBundle.getBundle(sharePanelOptionsKey)!!)
}