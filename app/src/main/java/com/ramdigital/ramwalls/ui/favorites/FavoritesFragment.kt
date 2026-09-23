package com.ramdigital.ramwalls.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ramdigital.ramwalls.adapter.WallpaperAdapter
import com.ramdigital.ramwalls.R
import com.ramdigital.ramwalls.ads.InterstitialAdController
import com.ramdigital.ramwalls.databinding.FragmentFavoritesBinding
import com.ramdigital.ramwalls.ui.WallpaperDetailActivity
import com.ramdigital.ramwalls.viewmodel.WallpaperViewModel

/** Favorites screen: only favorited wallpapers, with an empty state. */
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WallpaperViewModel by activityViewModels()
    private lateinit var adapter: WallpaperAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WallpaperAdapter(
            onItemClick = { item ->
                InterstitialAdController.maybeShow(requireActivity()) {
                    startActivity(WallpaperDetailActivity.newIntent(requireContext(), item.wallpaper.id))
                }
            },
            onFavoriteClick = { item -> viewModel.toggleFavorite(item.wallpaper.id) }
        )
        binding.recyclerFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerFavorites.adapter = adapter
        binding.recyclerFavorites.setHasFixedSize(true)

        viewModel.favoriteWallpapers.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            val empty = items.isEmpty()
            binding.emptyState.visibility = if (empty) View.VISIBLE else View.GONE
            binding.recyclerFavorites.visibility = if (empty) View.GONE else View.VISIBLE
            binding.textSubtitle.text = getString(R.string.saved_count_format, items.size)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
