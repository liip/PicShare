package ch.liip.picshare.screens

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ch.liip.picshare.R
import ch.liip.picshare.sharing.openSharingPanel
import ch.liip.picshare.sharing.sharing.options.ImageUriKey
import ch.liip.picshare.sharing.sharing.options.PreviewOptions
import kotlinx.android.synthetic.main.activity_preview.*

internal class PreviewActivity : AppCompatActivity() {

    val SHARING_REQUEST_CODE = 1

    private lateinit var viewModel: PreviewViewModel

    private val layoutResource = R.layout.activity_preview


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PreviewViewModel::class.java)

        setContentView(layoutResource)

        leftIconImageView.setOnClickListener {
            finish()
        }

        bindViewModel()

        val imageUri = intent.getParcelableExtra<Uri>(ImageUriKey)

        intent.extras?.let {
            viewModel.reloadFrom(PreviewOptions(it), imageUri.toString(), contentResolver)
        }
    }

    private fun bindViewModel() {
        viewModel.previewTitle.observe(this, Observer { title: String ->
            titleTextView.text = title
        })

        viewModel.previewBitmap.observe(this, Observer { bitmap: Bitmap ->
            preview.setImageBitmap(bitmap)
        })

        viewModel.shareImage.observe(this, Observer {
            openSharingPanel(this, Uri.parse(viewModel.previewUriString), "", "", SHARING_REQUEST_CODE)
        })

        shareImage.setOnClickListener {
            viewModel.shareButtonClicked()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // No check for result code. the sharing intent always returns 'cancelled' even when sharing has been completed

        when (requestCode) {
            SHARING_REQUEST_CODE -> {
                finish()
            }
        }
    }
}
