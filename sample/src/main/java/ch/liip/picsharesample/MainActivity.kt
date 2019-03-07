package ch.liip.picsharesample


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import ch.liip.picshare.inlaying.InlayViewProvider
import ch.liip.picshare.sharing.sharing.options.CropOptions
import ch.liip.picshare.sharing.sharing.options.InlayOptions
import ch.liip.picshare.sharing.sharing.options.PreviewOptions
import ch.liip.picshare.sharing.startSharing
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.inlay.view.*
import java.util.*

class MainActivity : Activity() {

    private val layoutResource: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutResource)

        share_button.setOnClickListener {
            shareImage()
        }
    }

    private fun shareImage() {

        var cropOptions: CropOptions? = null
        if(crop_checkbox.isChecked) {
            cropOptions = CropOptions()
                    .setFixedSize(1024, 1024)
        }

        var inlayOptions: InlayOptions? = null
        if(inlay_checkbox.isChecked) {
            inlayOptions = InlayOptions(InlayCustomProvider(R.layout.inlay))
        }

        var previewOptions: PreviewOptions? = null
        if(preview_checkbox.isChecked) {
            previewOptions = PreviewOptions()
                    .setTitle(resources.getString(R.string.picshare_app_title))
        }


        startSharing(this, 1024, 1024, "my share panel", "my shared image", cropOptions, inlayOptions, previewOptions)
    }

    class InlayCustomProvider(viewResourceId: Int) : InlayViewProvider(viewResourceId) {

        private val date: Date = Date()

        override fun populate(view: View, context: Context) {
            view.textViewDemo.text = context.getString(R.string.shared_date, date)
        }
    }
}
