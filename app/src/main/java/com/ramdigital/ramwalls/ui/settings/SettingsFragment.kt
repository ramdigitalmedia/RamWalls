package com.ramdigital.ramwalls.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.ramdigital.ramwalls.R
import com.ramdigital.ramwalls.databinding.FragmentSettingsBinding
import com.ramdigital.ramwalls.databinding.ItemSettingsRowBinding

/** Settings screen: app version, share, rate and privacy policy. */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindRow(
            binding.rowShare,
            iconRes = R.drawable.ic_share_outline,
            title = getString(R.string.share_app)
        ) { shareApp() }

        bindRow(
            binding.rowRate,
            iconRes = R.drawable.ic_star,
            title = getString(R.string.rate_app)
        ) { rateApp() }

        bindRow(
            binding.rowPrivacy,
            iconRes = R.drawable.ic_shield,
            title = getString(R.string.privacy_policy)
        ) { openPrivacyPolicy() }

        // Version row: shows value, no chevron, not clickable.
        bindRow(
            binding.rowVersion,
            iconRes = R.drawable.ic_info,
            title = getString(R.string.app_version),
            value = appVersionName(),
            onClick = null
        )
    }

    private fun bindRow(
        row: ItemSettingsRowBinding,
        iconRes: Int,
        title: String,
        value: String? = null,
        onClick: (() -> Unit)?
    ) {
        row.iconRow.setImageResource(iconRes)
        row.titleRow.text = title
        if (value != null) {
            row.valueRow.text = value
            row.valueRow.visibility = View.VISIBLE
        } else {
            row.valueRow.visibility = View.GONE
        }
        if (onClick != null) {
            row.chevronRow.visibility = View.VISIBLE
            row.root.isClickable = true
            row.root.setOnClickListener { onClick() }
        } else {
            row.chevronRow.visibility = View.GONE
            row.root.isClickable = false
            row.root.setOnClickListener(null)
        }
    }

    private fun appVersionName(): String = try {
        val pkg = requireContext().packageName
        requireContext().packageManager.getPackageInfo(pkg, 0).versionName ?: "1.0"
    } catch (e: Exception) {
        "1.0"
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text))
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
    }

    private fun rateApp() {
        val pkg = requireContext().packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, "market://details?id=$pkg".toUri()))
        } catch (e: Exception) {
            // Play Store app not available — fall back to the web URL.
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$pkg".toUri()
                )
            )
        }
    }

    private fun openPrivacyPolicy() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_url))))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), R.string.privacy_policy, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
