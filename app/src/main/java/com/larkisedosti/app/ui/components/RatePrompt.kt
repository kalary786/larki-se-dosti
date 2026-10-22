package com.larkisedosti.app.ui.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

internal object RatePrompt {
    private const val PREFS = "larki_prompts"
    private const val SESSIONS = "sessions"
    private const val LAST_SHOWN = "rate_last"
    private const val DONE = "rate_done"
    const val MIN_SESSIONS = 3
    const val COOLDOWN_MS = 7L * 24 * 60 * 60 * 1000

    fun recordSession(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val sessions = prefs.getInt(SESSIONS, 0) + 1
        prefs.edit().putInt(SESSIONS, sessions).apply()
        val last = prefs.getLong(LAST_SHOWN, 0L)
        val done = prefs.getBoolean(DONE, false)
        val cooledDown = System.currentTimeMillis() - last >= COOLDOWN_MS
        return sessions >= MIN_SESSIONS && !done && cooledDown
    }

    fun markShown(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putLong(LAST_SHOWN, System.currentTimeMillis())
            .apply()
    }

    fun markRated(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(DONE, true)
            .putLong(LAST_SHOWN, System.currentTimeMillis())
            .apply()
    }

    fun openStore(context: Context) {
        val id = context.packageName
        val market = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$id"))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val web = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$id"),
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(market)
        } catch (_: ActivityNotFoundException) {
            try {
                context.startActivity(web)
            } catch (_: ActivityNotFoundException) {
                // No store or browser on this device.
            }
        }
    }
}
