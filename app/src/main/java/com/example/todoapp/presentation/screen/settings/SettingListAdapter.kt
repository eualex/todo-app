package com.example.todoapp.presentation.screen.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.todoapp.R

class SettingListAdapter(private val settingItemList: List<SettingSectionItem>) :
    Adapter<SettingListAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(settingItem: SettingSectionItem) {
            val settingItemText = view.findViewById<TextView>(R.id.tvSettingListItem)

            settingItemText.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    view.context,
                    settingItem.icon
                ),
                null,
                null,
                null,
            )

            settingItemText.text = settingItem.name

            if (settingItem.color != null) {
                settingItemText.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        settingItem.color
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.settings_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = settingItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(settingItemList[position])
    }
}