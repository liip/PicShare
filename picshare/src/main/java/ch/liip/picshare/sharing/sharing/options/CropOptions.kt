package ch.liip.picshare.sharing.sharing.options

import android.os.Bundle
import ch.liip.picshare.exceptions.InvalidOptionException


class CropOptions : BaseOptions {

    private val titleKey = "$BaseKey.crop_title_key"
    internal var hasTitle = optionBundle.containsKey(titleKey)
    var title: String?
        get() = optionBundle.getString(titleKey)
        private set(title) = optionBundle.putString(titleKey, title)

    private val aspectRatioKey = "$BaseKey.crop_aspect_ratio_key"

    var aspectRatio: Float
        get() = optionBundle.getFloat(aspectRatioKey)
        private set(value) = optionBundle.putFloat(aspectRatioKey, value)

    private val fixedSizeXKey = "$BaseKey.crop_fixed_size_x_key"
    var fixedSizeX: Int
        get() = optionBundle.getInt(fixedSizeXKey)
        private set(value) = optionBundle.putInt(fixedSizeXKey, value)

    private val fixedSizeYKey = "$BaseKey.crop_fixed_size_y_key"
    var fixedSizeY: Int
        get() = optionBundle.getInt(fixedSizeYKey)
        private set(value) = optionBundle.putInt(fixedSizeYKey, value)


    constructor() : super()
    internal constructor(bundle: Bundle) : super(bundle)

    internal fun hasFixedAssetRatio() = optionBundle.containsKey(aspectRatioKey)

    internal fun hasFixedSize() = optionBundle.containsKey(fixedSizeXKey) && optionBundle.containsKey(fixedSizeYKey)

    @SuppressWarnings("unused") // public api method
    fun setTitle(cropTitle: String): CropOptions {
        title = cropTitle
        return this
    }

    @SuppressWarnings("unused") // public api method
    fun setAspectRatio(cropAspectRatio: Float): CropOptions {
        if (hasFixedSize()) {
            throw InvalidOptionException("Trying to set aspect ratio when a fixed size is defined")
        }

        aspectRatio = cropAspectRatio
        return this
    }

    fun setFixedSize(fixedPixelsX: Int, fixedPixelsY: Int): CropOptions {
        if (hasFixedAssetRatio()) {
            throw InvalidOptionException("Trying to set fixed size ratio when a fixed aspect ratio is defined")
        }

        fixedSizeX = fixedPixelsX
        fixedSizeY = fixedPixelsY
        aspectRatio = fixedPixelsY.toFloat() / fixedPixelsX.toFloat()
        return this
    }
}