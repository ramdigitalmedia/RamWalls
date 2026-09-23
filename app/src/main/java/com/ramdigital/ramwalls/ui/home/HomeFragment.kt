package com.ramdigital.ramwalls.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ramdigital.ramwalls.R
import com.ramdigital.ramwalls.adapter.WallpaperAdapter
import com.ramdigital.ramwalls.ads.AdsConfig
import com.ramdigital.ramwalls.ads.BannerAdController
import com.ramdigital.ramwalls.ads.InterstitialAdController
import com.ramdigital.ramwalls.data.WallpaperRepository
import com.ramdigital.ramwalls.databinding.FragmentHomeBinding
import com.ramdigital.ramwalls.model.Categories
import com.ramdigital.ramwalls.ui.WallpaperDetailActivity
import com.ramdigital.ramwalls.viewmodel.WallpaperViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/** Home screen: search bar, category chips and a 2-column wallpaper grid. */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WallpaperViewModel by activityViewModels()
    private lateinit var adapter: WallpaperAdapter
    private var bannerAdController: BannerAdController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupChips()
        setupSearch()
        setupHero()
        setupBanner()
        observe()
    }

    private fun setupRecycler() {
        adapter = WallpaperAdapter(
            onItemClick = { item ->
                InterstitialAdController.maybeShow(requireActivity()) {
                    startActivity(WallpaperDetailActivity.newIntent(requireContext(), item.wallpaper.id))
                }
            },
            onFavoriteClick = { item -> viewModel.toggleFavorite(item.wallpaper.id) }
        )
        binding.recyclerWallpapers.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerWallpapers.adapter = adapter
        binding.recyclerWallpapers.setHasFixedSize(true)
    }

    private fun setupChips() {
        val group: ChipGroup = binding.chipGroupCategories
        Categories.ALL_ORDERED.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                isChecked = category == viewModel.selectedCategory()
                setChipBackgroundColorResource(com.ramdigital.ramwalls.R.color.chip_bg_color)
                setTextColor(resources.getColorStateList(com.ramdigital.ramwalls.R.color.chip_text_color, null))
                chipStrokeWidth = 0f
                isCheckedIconVisible = false
            }
            group.addView(chip)
        }
        group.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedChip = checkedIds.firstOrNull()?.let { group.findViewById<Chip>(it) }
            checkedChip?.let { viewModel.setCategory(it.text.toString()) }
        }
    }

    private fun setupSearch() {
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupHero() {
        binding.textCollectionCount.text = getString(
            R.string.collection_count_format,
            WallpaperRepository.getWallpapers().size
        )
        binding.textCategoryCount.text = getString(
            R.string.category_count_format,
            Categories.ALL_ORDERED.size - 1
        )
    }

    private fun setupBanner() {
        bannerAdController = BannerAdController(
            activity = requireActivity(),
            container = binding.adViewContainer,
            adUnitId = AdsConfig.HOME_BANNER_AD_UNIT_ID
        )
        bannerAdController?.load()
    }

    private fun observe() {
        viewModel.homeWallpapers.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            binding.emptyState.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerWallpapers.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
            binding.textFavoriteCount.text = getString(
                R.string.favorite_count_format,
                items.count { it.isFavorite }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // Favorites may have changed on the detail screen.
        viewModel.refreshFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerAdController?.destroy()
        bannerAdController = null
        _binding = null
    }
}
