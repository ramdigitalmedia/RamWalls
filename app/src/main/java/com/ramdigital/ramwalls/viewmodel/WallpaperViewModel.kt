package com.ramdigital.ramwalls.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ramdigital.ramwalls.data.FavoriteManager
import com.ramdigital.ramwalls.data.WallpaperRepository
import com.ramdigital.ramwalls.model.Categories
import com.ramdigital.ramwalls.model.WallpaperUi

/**
 * Shared ViewModel for the home and favorites screens.
 *
 * Exposes two derived lists that recompute whenever the search query,
 * selected category, or favorite set changes:
 *  - [homeWallpapers]     filtered catalog for the Home grid
 *  - [favoriteWallpapers] only favorited wallpapers
 */
class WallpaperViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteManager = FavoriteManager(application)
    private val allWallpapers = WallpaperRepository.getWallpapers()

    private val searchQuery = MutableLiveData("")
    private val selectedCategory = MutableLiveData(Categories.ALL)
    private val favoriteIds = MutableLiveData(favoriteManager.getFavorites())

    val homeWallpapers: LiveData<List<WallpaperUi>> = MediatorLiveData<List<WallpaperUi>>().apply {
        val recompute = { value = computeHome() }
        addSource(searchQuery) { recompute() }
        addSource(selectedCategory) { recompute() }
        addSource(favoriteIds) { recompute() }
    }

    val favoriteWallpapers: LiveData<List<WallpaperUi>> = MediatorLiveData<List<WallpaperUi>>().apply {
        addSource(favoriteIds) { value = computeFavorites() }
    }

    private fun computeHome(): List<WallpaperUi> {
        val query = searchQuery.value.orEmpty().trim()
        val category = selectedCategory.value ?: Categories.ALL
        val favs = favoriteIds.value.orEmpty()
        return allWallpapers
            .filter { category == Categories.ALL || it.category == category }
            .filter { query.isEmpty() || it.title.contains(query, ignoreCase = true) }
            .map { WallpaperUi(it, favs.contains(it.id)) }
    }

    private fun computeFavorites(): List<WallpaperUi> {
        val favs = favoriteIds.value.orEmpty()
        return allWallpapers
            .filter { favs.contains(it.id) }
            .map { WallpaperUi(it, true) }
    }

    fun setSearchQuery(query: String) {
        if (searchQuery.value != query) searchQuery.value = query
    }

    fun setCategory(category: String) {
        if (selectedCategory.value != category) selectedCategory.value = category
    }

    fun selectedCategory(): String = selectedCategory.value ?: Categories.ALL

    fun isFavorite(id: String): Boolean = favoriteIds.value.orEmpty().contains(id)

    /** Toggles a favorite and refreshes derived lists. Returns new state. */
    fun toggleFavorite(id: String): Boolean {
        val nowFavorite = favoriteManager.toggle(id)
        favoriteIds.value = favoriteManager.getFavorites()
        return nowFavorite
    }

    /** Re-reads favorites from storage (e.g. after returning from the detail screen). */
    fun refreshFavorites() {
        favoriteIds.value = favoriteManager.getFavorites()
    }
}
