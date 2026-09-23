package com.ramdigital.ramwalls.ads

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class BannerAdController(
    private val activity: Activity,
    private val container: FrameLayout,
    private val adUnitId: String
) {
    private var adView: AdView? = null

    fun load() {
        container.post {
            val adWidth = (container.width / activity.resources.displayMetrics.density)
                .toInt()
                .coerceAtLeast(320)

            adView?.destroy()
            adView = AdView(activity).apply {
                this.adUnitId = adUnitId
                setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth))
                loadAd(AdRequest.Builder().build())
            }

            container.removeAllViews()
            container.addView(
                adView,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }

    fun destroy() {
        adView?.destroy()
        adView = null
        container.removeAllViews()
    }
}
