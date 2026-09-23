package com.ramdigital.ramwalls.data

import com.ramdigital.ramwalls.R
import com.ramdigital.ramwalls.model.Categories
import com.ramdigital.ramwalls.model.Wallpaper

/**
 * Single source of truth for the wallpaper catalog.
 *
 * All images are local drawables in res/drawable (wallpaper_1 .. wallpaper_8).
 * Replace the placeholder drawables with your own images of the same name,
 * or add new entries here pointing at your drawable resource ids.
 */
object WallpaperRepository {

    private val wallpapers: List<Wallpaper> = listOf(
        Wallpaper("w1", "Forest Glow", Categories.NATURE, R.drawable.wallpaper_1),
        Wallpaper("w2", "Violet Dream", Categories.ABSTRACT, R.drawable.wallpaper_2),
        Wallpaper("w3", "Sweet Sunset", Categories.LOVE, R.drawable.wallpaper_3),
        Wallpaper("w4", "Deep Ocean", Categories.NATURE, R.drawable.wallpaper_4),
        Wallpaper("w5", "Rise & Shine", Categories.MOTIVATION, R.drawable.wallpaper_5),
        Wallpaper("w6", "Golden Hour", Categories.PREMIUM, R.drawable.wallpaper_6),
        Wallpaper("w7", "Cosmic Night", Categories.PREMIUM, R.drawable.wallpaper_7),
        Wallpaper("w8", "Crimson Bloom", Categories.LOVE, R.drawable.wallpaper_8),
        Wallpaper("w9", "Forest Glow", Categories.NATURE, R.drawable.wallpaper_9),
        Wallpaper("w10", "Neon Flux", Categories.ABSTRACT, R.drawable.wallpaper_10),
        Wallpaper("w11", "Rise Up", Categories.MOTIVATION, R.drawable.wallpaper_11),
        Wallpaper("w12", "Royal Velvet", Categories.PREMIUM, R.drawable.wallpaper_12)
    )

    /** All wallpapers in the catalog. */
    fun getWallpapers(): List<Wallpaper> = wallpapers

    /** Find one wallpaper by its stable id. */
    fun getById(id: String): Wallpaper? = wallpapers.firstOrNull { it.id == id }
}
