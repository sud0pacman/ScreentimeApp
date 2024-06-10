package com.sudo_pacman.screentime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppUsageAdapter(private val appUsageList: List<AppUsageInfo>) :
    RecyclerView.Adapter<AppUsageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIconImageView: ImageView = view.findViewById(R.id.appIconImageView)
        val appNameTextView: TextView = view.findViewById(R.id.appNameTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_usage_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appUsageInfo = appUsageList[position]
        holder.appIconImageView.setImageDrawable(appUsageInfo.appIcon)
        holder.appNameTextView.text = appUsageInfo.packageName
        holder.timeTextView.text = appUsageInfo.getFormattedTime()
    }

    override fun getItemCount() = appUsageList.size
}
