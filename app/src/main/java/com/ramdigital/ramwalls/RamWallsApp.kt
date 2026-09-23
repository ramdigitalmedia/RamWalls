package com.ramdigital.ramwalls

import android.app.Application
import com.google.android.gms.ads.MobileAds

class RamWallsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread {
            MobileAds.initialize(this) {}
        }.start()
    }
}
