package br.com.livroandroid.carros.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File

// https://developer.android.com/topic/performance/graphics/load-bitmap.html
class ImageUtils {
    companion object {

        private const val TAG = "carros_camera"

        private fun calculateInSampleSize(
                options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth

            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val halfHeight = height / 2
                val halfWidth = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            Log.d(TAG, "sampleSize $inSampleSize")

            return inSampleSize
        }

        fun resize(file: File, reqWidth: Int, reqHeight: Int): Bitmap {

            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)

            // Tamanho original
            var w = options.outWidth
            var h = options.outHeight
            Log.d(TAG, "Bitmap original: $w/$h px")

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeFile(file.absolutePath, options)

            // Tamanho original
            w = options.outWidth
            h = options.outHeight
            Log.d(TAG, "Bitmap resize: $w/$h px")

            return bitmap
        }
    }
}
