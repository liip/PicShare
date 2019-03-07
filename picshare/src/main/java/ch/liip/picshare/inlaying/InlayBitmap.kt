package ch.liip.picshare.inlaying

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import android.view.View
import androidx.core.graphics.scale


internal object InlayBitmap {

    fun withView(bitmap: Bitmap, view: View): Bitmap {
        view.measure(View.MeasureSpec.makeMeasureSpec(bitmap.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(bitmap.height, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val c = Canvas(bitmap)

        view.draw(c)
        return bitmap
    }


    fun withText(text: String, bitmap: Bitmap) {
        val canvas = Canvas(bitmap)

        val titleSize = 70f
        val horizontalMargin = 20f
        val topMargin = 40f

        val textPaint = TextPaint()
        textPaint.color = Color.WHITE
        textPaint.textSize = titleSize
        textPaint.setShadowLayer(10f, 0f, 0f, Color.BLACK)

        val titleHeight = textHeight(textPaint)
        canvas.drawText(text, 0, text.length, horizontalMargin, topMargin + titleHeight, textPaint)
    }

    fun withImage(backImage: Bitmap, newImage: Bitmap, height: Float, anchorX: Float, anchorY: Float) {

        val canvas = Canvas(backImage)

        val ratio = height / newImage.height.toFloat()
        val scaledBitmap = newImage.scale((newImage.width * ratio).toInt(), (newImage.height * ratio).toInt())

        val textPaint = TextPaint()

        canvas.drawBitmap(newImage, anchorX * backImage.width, anchorY * backImage.height, textPaint)
        scaledBitmap.recycle()
    }

    private fun textHeight(paint: TextPaint): Float {
        return -paint.fontMetrics.ascent
    }
}