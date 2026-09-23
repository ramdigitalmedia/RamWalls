package com.ramdigital.ramwalls.util

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.ramdigital.ramwalls.R
import java.io.File
import java.io.FileOutputStream

/**
 * Wallpaper actions backed entirely by local drawables — set, save and share.
 * No internet, no API. Handles scoped storage (Android 10+) and the legacy
 * external-storage path for Android 9 and below.
 */
object WallpaperActions {

    private const val ALBUM = "RamWalls"

    /** Renders any drawable (including vectors) into a Bitmap. */
    private fun drawableToBitmap(
        context: Context,
        @DrawableRes resId: Int,
        fallbackWidth: Int,
        fallbackHeight: Int
    ): Bitmap {
        val drawable = ContextCompat.getDrawable(context, resId)
            ?: error("Drawable not found: $resId")

        if (drawable is BitmapDrawable && drawable.bitmap != null) {
            return drawable.bitmap
        }

        val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: fallbackWidth
        val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: fallbackHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /** Renders a drawable at the device screen size for a crisp wallpaper. */
    private fun screenSizedBitmap(context: Context, @DrawableRes resId: Int): Bitmap {
        val metrics = context.resources.displayMetrics
        return drawableToBitmap(context, resId, metrics.widthPixels, metrics.heightPixels)
    }

    /** Sets the wallpaper via [WallpaperManager]. Returns true on success. */
    fun setWallpaper(context: Context, @DrawableRes resId: Int): Boolean {
        return try {
            val manager = WallpaperManager.getInstance(context)
            manager.setBitmap(screenSizedBitmap(context, resId))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /** Saves the wallpaper into the device gallery. Returns true on success. */
    fun saveToGallery(context: Context, @DrawableRes resId: Int, title: String): Boolean {
        val bitmap = screenSizedBitmap(context, resId)
        val fileName = "${sanitize(title)}_${System.currentTimeMillis()}.png"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveWithMediaStore(context, bitmap, fileName)
        } else {
            saveLegacy(context, bitmap, fileName)
        }
    }

    /** Android 10+ scoped-storage save via MediaStore — no permission needed. */
    private fun saveWithMediaStore(context: Context, bitmap: Bitmap, fileName: String): Boolean {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + File.separator + ALBUM
            )
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri: Uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: return false

        return try {
            resolver.openOutputStream(uri)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            } ?: return false
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            resolver.delete(uri, null, null)
            false
        }
    }

    /** Android 9 and below — write to public Pictures dir (needs WRITE permission). */
    private fun saveLegacy(context: Context, bitmap: Bitmap, fileName: String): Boolean {
        return try {
            val picturesDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                ALBUM
            )
            if (!picturesDir.exists()) picturesDir.mkdirs()
            val file = File(picturesDir, fileName)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            // Make it visible in the gallery immediately.
            MediaScannerConnection.scanFile(
                context, arrayOf(file.absolutePath), arrayOf("image/png"), null
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /** Shares the wallpaper image via a chooser, using FileProvider. */
    fun shareWallpaper(context: Context, @DrawableRes resId: Int, title: String) {
        try {
            val bitmap = screenSizedBitmap(context, resId)
            val imagesFolder = File(context.cacheDir, "images")
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "share_${sanitize(title)}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider", file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_text))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(
                Intent.createChooser(shareIntent, context.getString(R.string.share_via))
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sanitize(name: String): String =
        name.replace(Regex("[^A-Za-z0-9]+"), "_").trim('_').ifEmpty { "ramwalls" }
}
