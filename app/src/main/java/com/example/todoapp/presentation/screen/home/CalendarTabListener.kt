package com.example.todoapp.presentation.screen.home

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.todoapp.R
import com.google.android.material.tabs.TabLayout

class CalendarTabListener(private val context: Context): TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) {
        changeTabColor(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        changeTabColor(tab)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        changeTabColor(tab)
    }

    private fun changeTabColor(tab: TabLayout.Tab?) {
        if(tab == null) return

        val weekNumberDayText = tab.view.findViewById<TextView>(R.id.weekNumberDayText)
        val weekDayText = tab.view.findViewById<TextView>(R.id.weekDayText)

        val tabItemColor = if(tab.isSelected) R.color.green_200 else R.color.black

        weekDayText.setTextColor(ContextCompat.getColor(context, tabItemColor))
        weekNumberDayText.setTextColor(ContextCompat.getColor(context, tabItemColor))
    }
}