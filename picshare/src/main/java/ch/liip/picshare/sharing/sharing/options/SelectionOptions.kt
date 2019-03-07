package ch.liip.picshare.sharing.sharing.options

import android.os.Bundle

class SelectionOptions : BaseOptions {
    private val maxSizeXKey = "$BaseKey.max_size_x_key"
    var maxSizeX: Int
        get() = optionBundle.getInt(maxSizeXKey)
        private set(value) = optionBundle.putInt(maxSizeXKey, value)

    private val maxSizeYKey = "$BaseKey.max_size_y_key"
    var maxSizeY: Int
        get() = optionBundle.getInt(maxSizeYKey)
        private set(value) = optionBundle.putInt(maxSizeYKey, value)


    constructor(maxSizeX: Int, maxSizeY: Int) : super() {
        setMaxSize(maxSizeX, maxSizeY)
    }
    internal constructor(bundle: Bundle) : super(bundle)

    fun setMaxSize(x: Int, y: Int): SelectionOptions {
        maxSizeX = x
        maxSizeY = y
        return this
    }
}