package com.ramdigital.ramwalls.ads

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object InterstitialAdController {
    private var interstitialAd: InterstitialAd? = null
    private var loading = false
    private var openCount = 0

    fun preload(activity: Activity) {
        if (loading || interstitialAd != null) return
        loading = true
        InterstitialAd.load(
            activity,
            AdsConfig.INTERSTITIAL_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    loading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    loading = false
                }
            }
        )
    }

    fun maybeShow(activity: Activity, onContinue: () -> Unit) {
        openCount += 1
        val ad = interstitialAd
        if (ad == null || openCount % 3 != 0) {
            preload(activity)
            onContinue()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                preload(activity)
                onContinue()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                interstitialAd = null
                preload(activity)
                onContinue()
            }
        }
        ad.show(activity)
    }
}
