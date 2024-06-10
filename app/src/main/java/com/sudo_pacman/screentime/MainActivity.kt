package com.sudo_pacman.screentime

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appUsageAdapter: AppUsageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (!hasUsageStatsPermission()) {
            requestUsageStatsPermission()
        } else {
            displayUsageStats()
        }
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOps.checkOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(), packageName
        )
        return mode == android.app.AppOpsManager.MODE_ALLOWED
    }

    private fun requestUsageStatsPermission() {
        Toast.makeText(this, "Grant Usage Access Permission", Toast.LENGTH_LONG).show()
        startActivity(Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    private fun displayUsageStats() {
        val usageStatsList = getUsageStats()
        val withOutUnused = usageStatsList
            .filter { it.timeInForeground / 1000 > 0 } // Cancel less than 0
            .sortedBy { it.timeInForeground } // sorting by time, bottom to top
            .reversed()  // reverse top to bottom

        appUsageAdapter = AppUsageAdapter(withOutUnused)
        recyclerView.adapter = appUsageAdapter
    }

    private fun getUsageStats(): List<AppUsageInfo> {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager


        // Calculate start time from midnight today
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTime = calendar.timeInMillis

        val endTime = System.currentTimeMillis()

        val usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startTime, endTime
        )


        val appUsageInfoList = mutableListOf<AppUsageInfo>()
        usageStatsList?.let {
            for (usageStats in it) {
                val appName = usageStats.packageName
                val totalTime = usageStats.totalTimeInForeground
                val appIcon: Drawable = try {
                    packageManager.getApplicationIcon(appName)
                } catch (e: PackageManager.NameNotFoundException) {
                    packageManager.defaultActivityIcon
                }
                appUsageInfoList.add(AppUsageInfo(appName, totalTime, appIcon))
            }
        }
        return appUsageInfoList
    }
}
