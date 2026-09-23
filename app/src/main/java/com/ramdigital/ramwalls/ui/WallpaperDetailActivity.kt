package com.ramdigital.ramwalls.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ramdigital.ramwalls.R
import com.ramdigital.ramwalls.data.WallpaperRepository
import com.ramdigital.ramwalls.databinding.ActivityWallpaperDetailBinding
import com.ramdigital.ramwalls.model.Wallpaper
import com.ramdigital.ramwalls.util.WallpaperActions
import com.ramdigital.ramwalls.viewmodel.WallpaperViewModel

/** Full-screen wallpaper preview with set / save / share actions. */
class WallpaperDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWallpaperDetailBinding
    private val viewModel: WallpaperViewModel by viewModels()
    private lateinit var wallpaper: Wallpaper

    private val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                saveWallpaper()
            } else {
                toast(getString(R.string.storage_permission_needed))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_WALLPAPER_ID)
        val loaded = id?.let { WallpaperRepository.getById(it) }
        if (loaded == null) {
            finish()
            return
        }
        wallpaper = loaded

        bindContent()
        setupActions()
    }

    private fun bindContent() {
        binding.imagePreview.setImageResource(wallpaper.resId)
        binding.textTitle.text = wallpaper.title
        binding.textCategory.text = wallpaper.category
        updateFavoriteIcon()
    }

    private fun setupActions() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite(wallpaper.id)
            updateFavoriteIcon()
        }

        binding.btnSetWallpaper.setOnClickListener {
            val success = WallpaperActions.setWallpaper(this, wallpaper.resId)
            toast(getString(if (success) R.string.wallpaper_set_success else R.string.wallpaper_set_failed))
        }

        binding.btnSave.setOnClickListener { requestSaveThenSave() }

        binding.btnShare.setOnClickListener {
            WallpaperActions.shareWallpaper(this, wallpaper.resId, wallpaper.title)
        }
    }

    private fun updateFavoriteIcon() {
        val isFav = viewModel.isFavorite(wallpaper.id)
        binding.iconFavorite.setImageResource(
            if (isFav) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
        )
    }

    /** On Android 9 and below we need WRITE_EXTERNAL_STORAGE; on 10+ none is needed. */
    private fun requestSaveThenSave() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                return
            }
        }
        saveWallpaper()
    }

    private fun saveWallpaper() {
        val success = WallpaperActions.saveToGallery(this, wallpaper.resId, wallpaper.title)
        toast(getString(if (success) R.string.saved_success else R.string.save_failed))
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_WALLPAPER_ID = "extra_wallpaper_id"

        fun newIntent(context: Context, wallpaperId: String): Intent =
            Intent(context, WallpaperDetailActivity::class.java).apply {
                putExtra(EXTRA_WALLPAPER_ID, wallpaperId)
            }
    }
}
