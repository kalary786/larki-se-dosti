package com.larkisedosti.app.data

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions

/**
 * Google's sample rewarded unit. Replace it with the real AdMob unit, and set that
 * unit's server-side verification URL to the deployed admobSsv function.
 * The phone never grants free messages. AdMob calls the server after a completed view.
 */
object MessageRewardAd {
    private const val REWARDED_UNIT = "ca-app-pub-3940256099942544/5224354917"
    private var started = false

    fun show(activity: Activity, uid: String, onFinished: (Boolean) -> Unit) {
        if (uid.isBlank()) {
            onFinished(false)
            return
        }
        val load = {
            RewardedAd.load(
                activity,
                REWARDED_UNIT,
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        ad.setServerSideVerificationOptions(
                            ServerSideVerificationOptions.Builder()
                                .setUserId(uid)
                                .setCustomData("messages")
                                .build(),
                        )
                        ad.fullScreenContentCallback = object : FullScreenContentCallback() {}
                        ad.show(activity) { _ -> }
                        onFinished(true)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        onFinished(false)
                    }
                },
            )
        }
        if (started) {
            load()
        } else {
            MobileAds.initialize(activity) {
                started = true
                load()
            }
        }
    }
}
