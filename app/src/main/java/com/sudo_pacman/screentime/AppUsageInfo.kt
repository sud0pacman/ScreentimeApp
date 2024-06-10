package com.sudo_pacman.screentime

import android.graphics.drawable.Drawable

data class AppUsageInfo(
    val packageName: String,
    val timeInForeground: Long,
    val appIcon: Drawable
) {
    fun getFormattedTime(): String {
        val seconds = (timeInForeground / 1000) % 60
        val minutes = (timeInForeground / (1000 * 60)) % 60
        val hours = timeInForeground / (1000 * 60 * 60)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
