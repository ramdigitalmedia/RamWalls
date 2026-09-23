package com.ramdigital.ramwalls.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramdigital.ramwalls.R
import com.ramdigital.ramwalls.databinding.ItemWallpaperBinding
import com.ramdigital.ramwalls.model.WallpaperUi

/**
 * Grid adapter for wallpaper cards. Uses [ListAdapter] + DiffUtil so favorite
 * toggles and category/search changes animate smoothly.
 */
class WallpaperAdapter(
    private val onItemClick: (WallpaperUi) -> Unit,
    private val onFavoriteClick: (WallpaperUi) -> Unit
) : ListAdapter<WallpaperUi, WallpaperAdapter.WallpaperViewHolder>(DIFF) {

    inner class WallpaperViewHolder(
        private val binding: ItemWallpaperBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WallpaperUi) {
            val wallpaper = item.wallpaper
            binding.imageWallpaper.setImageResource(wallpaper.resId)
            binding.textTitle.text = wallpaper.title
            binding.textCategory.text = wallpaper.category
            binding.textBadge.text = if (wallpaper.category == "Premium") {
                "Premium"
            } else {
                wallpaper.category.uppercase()
            }
            binding.btnFavorite.setImageResource(
                if (item.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
            )
            binding.btnFavorite.imageTintList = ContextCompat.getColorStateList(
                binding.root.context,
                if (item.isFavorite) R.color.heart_active else R.color.text_primary
            )
            binding.root.setOnClickListener { onItemClick(item) }
            binding.btnFavorite.setOnClickListener { onFavoriteClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val binding = ItemWallpaperBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WallpaperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<WallpaperUi>() {
            override fun areItemsTheSame(oldItem: WallpaperUi, newItem: WallpaperUi): Boolean =
                oldItem.wallpaper.id == newItem.wallpaper.id

            override fun areContentsTheSame(oldItem: WallpaperUi, newItem: WallpaperUi): Boolean =
                oldItem == newItem
        }
    }
}
