package ch.liip.picshare.sharing

import ch.liip.picshare.sharing.sharing.options.BaseOptions
import ch.liip.picshare.sharing.sharing.options.SharingOptions

internal const val currentStateRestoreKey = "ch.liip.picshare.state_restore_key"

internal class SharingFlow constructor (
        private val sharingOptions: SharingOptions,
        private var currentState: State,
        private val callback: (options: BaseOptions) -> Unit
) {
    enum class State {Selection, Crop, Inlay, Preview, Share}

    fun sharingOptions() = sharingOptions

    fun currentState() = currentState

    fun call() {
        callState(currentState)
    }

    fun pictureSelectionCompleted() {
        when {
            sharingOptions.isCropEnabled -> callState(State.Crop)
            sharingOptions.isInlayEnabled -> callState(State.Inlay)
            sharingOptions.isPreviewEnabled -> callState(State.Preview)
            else -> callState(State.Share)
        }
    }

    fun cropCompleted() {
        when {
            sharingOptions.isInlayEnabled -> callState(State.Inlay)
            sharingOptions.isPreviewEnabled -> callState(State.Preview)
            else -> callState(State.Share)
        }
    }

    fun inlayCompleted() {
        when {
            sharingOptions.isPreviewEnabled -> callState(State.Preview)
            else -> callState(State.Share)
        }
    }

    private fun callState(state: State) {
        currentState = state
        when (currentState){
            State.Selection -> callback(sharingOptions.selectionOptions())
            State.Crop-> callback(sharingOptions.cropOptions())
            State.Inlay -> callback(sharingOptions.inlayOptions())
            State.Preview -> callback(sharingOptions.previewOptions())
            State.Share -> callback(sharingOptions.sharePanelOptions())
        }
    }
}