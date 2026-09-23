package com.fixredmagicwindow.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object PatchKeys {
    const val RM_WINDOW_REPLY_LIMITS = "patch_rm_window_reply_limits"
    const val DISABLE_WR_AUTO_HANG = "patch_disable_wr_auto_hang"
    const val PREVENT_WR_MINI_TO_HANG_BUBBLE = "patch_prevent_wr_mini_to_hang_bubble"
    const val DISABLE_WR_EDGE_CLAMP = "patch_disable_wr_edge_clamp"
    const val DISABLE_WR_DRAG_TO_SPLIT = "patch_disable_wr_drag_to_split"

    fun defaultFor(key: String): Boolean = key == RM_WINDOW_REPLY_LIMITS
}

object PatchPrefs {
    private const val PREFS_NAME = "FixRedMagicWindowConfig"

    //LSPosed's "xposedsharedprefs" support makes MODE_WORLD_READABLE legal for an active
    //module so the system_server hooks can read it; it throws if the module isn't active yet.
    @SuppressLint("WorldReadableFiles")
    private fun prefs(context: Context): SharedPreferences = try {
        @Suppress("DEPRECATION")
        context.getSharedPreferences(PREFS_NAME, Context.MODE_WORLD_READABLE)
    } catch (_: SecurityException) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isEnabled(context: Context, key: String): Boolean =
        prefs(context).getBoolean(key, PatchKeys.defaultFor(key))

    fun setEnabled(context: Context, key: String, enabled: Boolean) {
        prefs(context).edit().putBoolean(key, enabled).apply()
    }
}
