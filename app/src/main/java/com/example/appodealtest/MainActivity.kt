package com.example.appodealtest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.appodeal.ads.Appodeal
import com.appodeal.ads.BannerCallbacks
import com.appodeal.ads.InterstitialCallbacks
import com.appodeal.ads.NativeAd
import com.appodeal.ads.NativeCallbacks
import com.appodeal.ads.RewardedVideoCallbacks
import com.appodeal.ads.initializing.ApdInitializationCallback
import com.appodeal.ads.initializing.ApdInitializationError
import com.appodeal.ads.nativead.NativeAdViewNewsFeed
import com.appodeal.ads.utils.Log.LogLevel

/** Small, deliberately explicit sample for exercising all requested Appodeal formats. */
class MainActivity : AppCompatActivity() {
    private lateinit var status: TextView
    private lateinit var events: TextView
    private lateinit var nativeAdView: NativeAdViewNewsFeed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        status = findViewById(R.id.status)
        events = findViewById(R.id.events)
        nativeAdView = findViewById(R.id.native_ad)

        findViewById<Button>(R.id.show_banner).setOnClickListener { show(Appodeal.BANNER_VIEW, "banner") }
        findViewById<Button>(R.id.show_interstitial).setOnClickListener { show(Appodeal.INTERSTITIAL, "interstitial") }
        findViewById<Button>(R.id.show_rewarded).setOnClickListener { show(Appodeal.REWARDED_VIDEO, "rewarded") }
        findViewById<Button>(R.id.show_native).setOnClickListener { showNative() }

        installCallbacks()
        // All settings that affect startup are intentionally before initialize().
        Appodeal.setTesting(true)
        Appodeal.setLogLevel(LogLevel.verbose)
        Appodeal.setBannerViewId(R.id.appodeal_banner)
        Appodeal.setAutoCache(Appodeal.BANNER, false)
        Appodeal.setAutoCache(Appodeal.INTERSTITIAL, false)
        Appodeal.setAutoCache(Appodeal.REWARDED_VIDEO, false)
        Appodeal.setAutoCache(Appodeal.NATIVE, false)

        if (BuildConfig.APPODEAL_APP_KEY.isBlank()) {
            status.text = "Missing APPODEAL_APP_KEY (see README)"
            event("INIT SKIPPED: no Appodeal app key configured")
            return
        }
        val formats = Appodeal.BANNER or Appodeal.INTERSTITIAL or
            Appodeal.REWARDED_VIDEO or Appodeal.NATIVE
        Appodeal.initialize(this, BuildConfig.APPODEAL_APP_KEY, formats,
            object : ApdInitializationCallback {
                override fun onInitializationFinished(errors: List<ApdInitializationError>?) {
                    event("INIT finished; errors=${errors ?: "none"}")
                    status.text = if (errors.isNullOrEmpty()) "Initialized; loading all formats" else "Initialized with errors"
                    loadAll()
                }
            })
    }

    private fun loadAll() {
        event("LOAD banner"); Appodeal.cache(this, Appodeal.BANNER)
        event("LOAD interstitial"); Appodeal.cache(this, Appodeal.INTERSTITIAL)
        event("LOAD rewarded"); Appodeal.cache(this, Appodeal.REWARDED_VIDEO)
        event("LOAD native"); Appodeal.cache(this, Appodeal.NATIVE)
    }

    private fun show(type: Int, name: String) {
        val loaded = Appodeal.isLoaded(type)
        event("SHOW $name requested; loaded=$loaded")
        if (!loaded) {
            event("SHOW $name skipped; cache again")
            Appodeal.cache(this, type)
            return
        }
        event("SHOW $name accepted=${Appodeal.show(this, type)}")
    }

    private fun showNative() {
        event("SHOW native requested; loaded=${Appodeal.isLoaded(Appodeal.NATIVE)}")
        val ad = Appodeal.getNativeAds(1).firstOrNull()
        if (ad == null) {
            event("SHOW native skipped; cache again")
            Appodeal.cache(this, Appodeal.NATIVE)
        } else {
            event("SHOW native accepted=${nativeAdView.registerView(ad)}")
        }
    }

    private fun installCallbacks() {
        Appodeal.setBannerCallbacks(object : BannerCallbacks {
            override fun onBannerLoaded(height: Int, isPrecache: Boolean) = event("BANNER loaded height=$height precache=$isPrecache")
            override fun onBannerFailedToLoad() = event("BANNER failedToLoad")
            override fun onBannerShown() = event("BANNER shown")
            override fun onBannerShowFailed() = event("BANNER showFailed")
            override fun onBannerClicked() = event("BANNER clicked")
            override fun onBannerExpired() = event("BANNER expired")
        })
        Appodeal.setInterstitialCallbacks(object : InterstitialCallbacks {
            override fun onInterstitialLoaded(isPrecache: Boolean) = event("INTERSTITIAL loaded precache=$isPrecache")
            override fun onInterstitialFailedToLoad() = event("INTERSTITIAL failedToLoad")
            override fun onInterstitialShown() = event("INTERSTITIAL shown")
            override fun onInterstitialShowFailed() = event("INTERSTITIAL showFailed")
            override fun onInterstitialClicked() = event("INTERSTITIAL clicked")
            override fun onInterstitialClosed() = event("INTERSTITIAL closed")
            override fun onInterstitialExpired() = event("INTERSTITIAL expired")
        })
        Appodeal.setRewardedVideoCallbacks(object : RewardedVideoCallbacks {
            override fun onRewardedVideoLoaded(isPrecache: Boolean) = event("REWARDED loaded precache=$isPrecache")
            override fun onRewardedVideoFailedToLoad() = event("REWARDED failedToLoad")
            override fun onRewardedVideoShown() = event("REWARDED shown")
            override fun onRewardedVideoShowFailed() = event("REWARDED showFailed")
            override fun onRewardedVideoClicked() = event("REWARDED clicked")
            override fun onRewardedVideoFinished(amount: Double, currency: String) = event("REWARDED finished amount=$amount currency=$currency")
            override fun onRewardedVideoClosed(finished: Boolean) = event("REWARDED closed finished=$finished")
            override fun onRewardedVideoExpired() = event("REWARDED expired")
        })
        Appodeal.setNativeCallbacks(object : NativeCallbacks {
            override fun onNativeLoaded() = event("NATIVE loaded")
            override fun onNativeFailedToLoad() = event("NATIVE failedToLoad")
            override fun onNativeShown(nativeAd: NativeAd?) = event("NATIVE shown")
            override fun onNativeShowFailed(nativeAd: NativeAd?) = event("NATIVE showFailed")
            override fun onNativeClicked(nativeAd: NativeAd?) = event("NATIVE clicked")
            override fun onNativeExpired() = event("NATIVE expired")
        })
    }

    private fun event(message: String) {
        Log.i("AppodealSample", message)
        runOnUiThread { events.text = "${System.currentTimeMillis()}  $message\n${events.text}" }
    }

    override fun onDestroy() {
        nativeAdView.destroy()
        super.onDestroy()
    }
}
