package ch.liip.picshare.sharing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.math.MathUtils
import ch.liip.picshare.R
import ch.liip.picshare.exceptions.InvalidOptionException
import ch.liip.picshare.helpers.*
import ch.liip.picshare.inlaying.InlayBitmap
import ch.liip.picshare.screens.PreviewActivity
import ch.liip.picshare.sharing.sharing.options.*
import com.yalantis.ucrop.UCrop
import kotlin.math.min


internal class SharingActivity : AppCompatActivity() {

    private val pickImageRequestKey = 0
    private val cropImageRequestKey = 1
    private val previewImageRequestKey = 2
    private val sharingImageRequestKey = 3

    private lateinit var flow: SharingFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createEmptyCacheFile(this)

        // We can't work without options, so fail early because that would be a bug in the lib to get a null bundle
        val options = SharingOptions(intent.extras!!)
        val savedState = savedInstanceState?.get(currentStateRestoreKey) as? SharingFlow.State

        if(savedState == null) {
            flow = SharingFlow(options, SharingFlow.State.Selection, this::flowListener)
            flow.call()
        }
        else {
            flow = SharingFlow(options, savedState, this::flowListener)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(currentStateRestoreKey, flow.currentState())
    }

    private fun flowListener(options: BaseOptions) {
        when(options) {
            is SelectionOptions -> startImageSelection()
            is CropOptions -> startCrop(options)
            is InlayOptions -> startInlay(options)
            is PreviewOptions -> startPreview(options)
            is SharePanelOptions -> shareNow(options)
        }
    }

    private fun startImageSelection() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, ""), pickImageRequestKey)
    }

    private fun startCrop(cropOptions: CropOptions) {

        val color1 = primaryColor(resources)
        val color2 = secondaryColor(resources)
        val color3 = textColor(resources)

        val options = UCrop.Options().apply {

            setToolbarTitle(if (cropOptions.hasTitle) cropOptions.title else null)
            setActiveWidgetColor(color1)
            setToolbarColor(color1)
            setStatusBarColor(color1)
            setCropFrameColor(color2)
            setCropGridColor(color2)
            setToolbarWidgetColor(color3)

            setHideBottomControls(true)
            setFreeStyleCropEnabled(!cropOptions.hasFixedAssetRatio())
        }

        val uri = localImageUri(this)
        val crop = UCrop.of(uri, uri)
                .withOptions(options)

        if (cropOptions.hasFixedAssetRatio()) {
            crop.withAspectRatio(1f, cropOptions.aspectRatio)
        }

        if (cropOptions.hasFixedSize()) {
            crop.withMaxResultSize(cropOptions.fixedSizeX, cropOptions.fixedSizeY)
        }

        crop.start(this, cropImageRequestKey)
    }

    private fun startInlay(inlayOptions: InlayOptions) {

        val uri = localImageUri(this)
        inlay(uri, uri, inlayOptions)
        flow.inlayCompleted()
    }

    private fun startPreview(previewOptions: PreviewOptions) {

        val sharingPreviewIntent = Intent(this, PreviewActivity::class.java)
        sharingPreviewIntent.putExtras(previewOptions.bundle())
        sharingPreviewIntent.putExtra(ImageUriKey, localImageUri(this))

        startActivityForResult(sharingPreviewIntent, previewImageRequestKey)
    }

    private fun shareNow(options: SharePanelOptions) {
        val uri = localImageUri(this)
        val panelTitle = options.shareTitle
        val imageTitle = options.imageTitle
        openSharingPanel(this, uri, imageTitle, panelTitle, sharingImageRequestKey)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            close()
            return
        }

        when (requestCode) {
            pickImageRequestKey -> {
                val selectedUri = data?.data!!

                val destUri = localImageUri(this)
                scaleImageToFit(flow.sharingOptions().selectionOptions().maxSizeX,
                        flow.sharingOptions().selectionOptions().maxSizeY, selectedUri, destUri)

                flow.pictureSelectionCompleted()
            }

            cropImageRequestKey -> {
                val uri = localImageUri(this)
                val cropOptions = flow.sharingOptions().cropOptions()
                if (cropOptions.hasFixedSize()) {
                    scaleImage(cropOptions.fixedSizeX, cropOptions.fixedSizeY, uri, uri)
                }

                flow.cropCompleted()
            }

            previewImageRequestKey -> {
                close()
            }

            sharingImageRequestKey -> {
                // Warning, this might not be called, as sharing usually returns a Result_cancel status
                // which would be caught by the RESULT_OK test above
                close()
            }
        }
    }

    private fun scaleImageToFit(x: Int, y: Int, srcUri: Uri, destUri: Uri) {
        val bitmap = load(contentResolver, srcUri, false)

        val xRatio = x.toFloat() / bitmap.width.toFloat()
        val yRatio = y.toFloat() / bitmap.height.toFloat()
        val ratio = MathUtils.clamp(min(xRatio, yRatio), 0f, 1f) // Clamp because we only want to scale down

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, (ratio * bitmap.width).toInt(), (ratio * bitmap.height).toInt(), false)
        save(scaledBitmap, destUri)

        bitmap.recycle()
        scaledBitmap.recycle()
    }

    private fun scaleImage(x: Int, y: Int, srcUri: Uri, destUri: Uri) {
        val bitmap = load(contentResolver, srcUri, false)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, x, y, false)
        save(scaledBitmap, destUri)

        bitmap.recycle()
        scaledBitmap.recycle()
    }

    private fun inlay(sourceUri: Uri, destUri: Uri, inlayOptions: InlayOptions): Boolean {

        val mutableBitmap = load(contentResolver, sourceUri, true)

        //InlayBitmap.withText("DEMO Text", scaledMutableBitmap)

        val provider = inlayOptions.inlayViewProvider
        val view = layoutInflater.inflate(provider.viewResourceId, null)
        provider.populate(view, this)
        InlayBitmap.withView(mutableBitmap, view)

        val success = save(mutableBitmap, destUri)

        mutableBitmap.recycle()

        return success
    }

    private fun close() {
        destroyCacheFile(this)
        finish()
    }
}


internal fun openSharingPanel(activity: Activity, imageUri: Uri, imageTitle: String, appChooserTitle: String, requestCode: Int) {

    // Only uri of type content:// can be shared across apps.
    // Test the Uri and attempt to convert if needed

    val sharableImageUri: Uri = if (imageUri.toString().startsWith("content://"))
        imageUri
    else
        sharableUri(activity, imageUri)

    val intent = ShareCompat.IntentBuilder.from(activity)
            .setType("image/*")
            .setSubject(imageTitle)
            .setStream(sharableImageUri)
            .setChooserTitle(appChooserTitle)
            .createChooserIntent()

    activity.startActivityForResult(intent, requestCode)
}

fun startSharing(context: Context, maxSizeX: Int, maxSizeY: Int, panelTitle: String, imageName: String, cropOptions: CropOptions? = null, inlayOptions: InlayOptions? = null, previewOptions: PreviewOptions? = null) {
    val imageSharingIntent = Intent(context, SharingActivity::class.java)

    val selectionOptions = SelectionOptions(maxSizeX, maxSizeY)
    val sharePanelOptions = SharePanelOptions(panelTitle, imageName)

    if(cropOptions != null && cropOptions.hasFixedSize()
    && (cropOptions.fixedSizeX > selectionOptions.maxSizeX || cropOptions.fixedSizeY > selectionOptions.maxSizeY)) {
        throw InvalidOptionException("Max size can't be smaller than crop size")
    }

    val options = SharingOptions(selectionOptions, sharePanelOptions)
            .setCrop(cropOptions)
            .setInlay(inlayOptions)
            .setPreview(previewOptions)

    imageSharingIntent.putExtras(options.bundle())

    context.startActivity(imageSharingIntent)
}

private fun primaryColor(resources: Resources) = ResourcesCompat.getColor(resources, R.color.primary_color, null)
private fun secondaryColor(resources: Resources) = ResourcesCompat.getColor(resources, R.color.secondary_color, null)
private fun textColor(resources: Resources) = ResourcesCompat.getColor(resources, R.color.text_color, null)
