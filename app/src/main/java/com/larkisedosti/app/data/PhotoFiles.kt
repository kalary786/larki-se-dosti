package com.larkisedosti.app.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.ByteArrayOutputStream
import kotlin.math.max
import kotlin.math.roundToInt

object PhotoFiles {
    private const val maxWidth = 1080
    private const val targetBytes = 300 * 1024

    fun jpeg(context: Context, uri: Uri): ByteArray {
        val resolver = context.contentResolver
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
            ?: error("Couldn't read that photo.")
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) error("Couldn't read that photo.")
        val orientation = runCatching {
            resolver.openInputStream(uri)?.use { stream ->
                ExifInterface(stream).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL,
                )
            }
        }.getOrNull() ?: ExifInterface.ORIENTATION_NORMAL
        val sample = sampleSize(bounds.outWidth, bounds.outHeight)
        val decoded = resolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(
                stream,
                null,
                BitmapFactory.Options().apply { inSampleSize = sample },
            )
        } ?: error("Couldn't read that photo.")
        val rotated = rotate(decoded, orientation)
        val cropped = centerCropFourByFive(rotated)
        var current = scaleToWidth(cropped, maxWidth)
        var bytes = compress(current)
        var guard = 0
        while (bytes.size > targetBytes + 20 * 1024 && current.width > 480 && guard < 4) {
            current = scaleToWidth(current, (current.width * 0.85f).roundToInt())
            bytes = compress(current)
            guard += 1
        }
        if (!current.isRecycled) current.recycle()
        return bytes
    }

    private fun sampleSize(width: Int, height: Int): Int {
        var sample = 1
        val edge = max(width, height)
        while (edge / sample > 2400) sample *= 2
        return sample
    }

    private fun rotate(source: Bitmap, orientation: Int): Bitmap {
        val degrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> return source
        }
        val matrix = Matrix().apply { postRotate(degrees) }
        val rotated = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        if (rotated != source) source.recycle()
        return rotated
    }

    private fun centerCropFourByFive(source: Bitmap): Bitmap {
        val target = 4f / 5f
        val current = source.width.toFloat() / source.height.toFloat()
        val cropped = if (current > target) {
            val width = (source.height * target).roundToInt().coerceAtLeast(1)
            val x = ((source.width - width) / 2).coerceAtLeast(0)
            Bitmap.createBitmap(source, x, 0, width.coerceAtMost(source.width - x), source.height)
        } else {
            val height = (source.width / target).roundToInt().coerceAtLeast(1)
            val y = ((source.height - height) / 2).coerceAtLeast(0)
            Bitmap.createBitmap(source, 0, y, source.width, height.coerceAtMost(source.height - y))
        }
        if (cropped != source) source.recycle()
        return cropped
    }

    private fun scaleToWidth(source: Bitmap, width: Int): Bitmap {
        if (source.width <= width) return source
        val height = (source.height * (width.toFloat() / source.width)).roundToInt().coerceAtLeast(1)
        val scaled = Bitmap.createScaledBitmap(source, width, height, true)
        if (scaled != source) source.recycle()
        return scaled
    }

    private fun compress(source: Bitmap): ByteArray {
        var quality = 90
        var bytes: ByteArray
        do {
            val stream = ByteArrayOutputStream()
            source.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            bytes = stream.toByteArray()
            quality -= 8
        } while (bytes.size > targetBytes && quality >= 42)
        return bytes
    }
}
