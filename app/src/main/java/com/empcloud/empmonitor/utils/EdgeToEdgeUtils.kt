package com.empcloud.empmonitor.utils

import android.app.Activity
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Centralised edge-to-edge handling.
 *
 * Android 16 (targetSdk 36) force-enables edge-to-edge and ignores the legacy
 * `android:windowOptOutEdgeToEdgeEnforcement` and `android:statusBarColor` flags.
 * As a result, content is drawn behind the status bar and navigation bar and
 * overlaps them. This helper makes every screen render edge-to-edge consistently
 * across all supported API levels (minSdk 24) and applies the system-bar,
 * display-cutout and IME insets as padding to the activity's root content view so
 * nothing overlaps the system bars or the keyboard.
 */
object EdgeToEdgeUtils {

    /**
     * @param lightStatusBarIcons true -> dark status-bar icons (use on light
     * backgrounds), false -> light status-bar icons (use on dark backgrounds).
     */
    fun apply(activity: Activity, lightStatusBarIcons: Boolean = true) {
        val window = activity.window

        // Force edge-to-edge on every API level so behaviour is identical
        // everywhere (instead of relying on the now-ignored opt-out flag).
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = lightStatusBarIcons
            isAppearanceLightNavigationBars = true
        }

        val root = activity.findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(bars.left, bars.top, bars.right, maxOf(bars.bottom, ime.bottom))
            // Consume so child views with fitsSystemWindows="true" don't double-pad.
            WindowInsetsCompat.CONSUMED
        }
        ViewCompat.requestApplyInsets(root)
    }
}
