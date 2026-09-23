package com.ramdigital.ramwalls.data

import android.content.Context

/**
 * Persists favorite wallpaper ids in SharedPreferences.
 *
 * Stores the favorites as a String set keyed by [KEY_FAVORITES]. We always
 * write a brand-new set instance because mutating the set returned by
 * [android.content.SharedPreferences.getStringSet] has undefined behavior.
 */
class FavoriteManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** Current favorite ids (defensive copy). */
    fun getFavorites(): Set<String> =
        prefs.getStringSet(KEY_FAVORITES, emptySet())?.toSet() ?: emptySet()

    fun isFavorite(id: String): Boolean = getFavorites().contains(id)

    /** Toggles the id and returns the new favorite state (true = now favorite). */
    fun toggle(id: String): Boolean {
        val current = getFavorites().toMutableSet()
        val nowFavorite = if (current.contains(id)) {
            current.remove(id)
            false
        } else {
            current.add(id)
            true
        }
        prefs.edit().putStringSet(KEY_FAVORITES, current).apply()
        return nowFavorite
    }

    companion object {
        private const val PREFS_NAME = "ramwalls_favorites"
        private const val KEY_FAVORITES = "favorite_ids"
    }
}
