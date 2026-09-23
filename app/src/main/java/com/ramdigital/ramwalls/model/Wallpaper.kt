package com.ramdigital.ramwalls.model

import androidx.annotation.DrawableRes

/**
 * A single wallpaper backed by a local drawable resource.
 *
 * @param id        Stable unique id, used as the favorites key.
 * @param title     Display title.
 * @param category  One of [Categories].
 * @param resId     Drawable resource id of the local image.
 */
data class Wallpaper(
    val id: String,
    val title: String,
    val category: String,
    @param:DrawableRes @field:DrawableRes val resId: Int,
    val isPremium: Boolean = false,
    val isFeatured: Boolean = false,
    val isTrending: Boolean = false,
    val downloadCount: Int = 0
)

/** A wallpaper paired with its current favorite state for the UI/adapter. */
data class WallpaperUi(
    val wallpaper: Wallpaper,
    val isFavorite: Boolean
)

/** Category labels. Keep in sync with strings.xml category names. */
object Categories {
    const val ALL = "All"
    const val CARS = "Cars"
    const val NATURE = "Nature"
    const val ANIME = "Anime"
    const val LOVE = "Love"
    const val MOTIVATION = "Motivation"
    const val AMOLED = "AMOLED"
    const val ABSTRACT = "Abstract"
    const val LUXURY = "Luxury"
    const val ANIMALS = "Animals"
    const val SPACE = "Space"
    const val SPORTS = "Sports"
    const val INDIAN = "Indian"
    const val FESTIVALS = "Festivals"
    const val PREMIUM = "Premium"

    val ALL_ORDERED = listOf(
        ALL, CARS, NATURE, ANIME, LOVE, MOTIVATION, AMOLED, ABSTRACT,
        LUXURY, ANIMALS, SPACE, SPORTS, INDIAN, FESTIVALS, PREMIUM
    )
}
