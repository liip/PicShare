package ch.liip.picshare.sharing.sharing.options

import android.os.Bundle

internal const val BaseKey = "ch.liip.picshare.keys"

abstract class BaseOptions() {

        protected var optionBundle = Bundle()

        constructor(bundle: Bundle) : this() {
            optionBundle = bundle
        }

        fun bundle(): Bundle = optionBundle
}