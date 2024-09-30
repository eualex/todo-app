package com.example.todoapp.presentation.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout

class HomeFragment: Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        setupCustomTabs()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCustomTabs() {
        val tabItems = mapOf(
            "Mon" to 30,
            "Tue" to 1,
            "Wed" to 2,
            "Thu" to 3,
            "Fri" to 4,
            "Sat" to 5,
            "Sun" to 6,
        )

        tabItems.keys.forEachIndexed { index, tabItemKey ->
            val tab = binding.weekTab.getTabAt(index)
            if(tab != null){
                val tabItemView = layoutInflater.inflate(R.layout.tab_icon_item, null)
                val weekNumberDayText = tabItemView.findViewById<TextView>(R.id.weekNumberDayText)
                val weekDayText = tabItemView.findViewById<TextView>(R.id.weekDayText)

                weekDayText.text = tabItemKey
                weekNumberDayText.text = tabItems.get(tabItemKey).toString().padStart(2, '0')
                tab.customView = tabItemView

                val tabItemColor = if(tab.isSelected) R.color.green_200 else R.color.black

                weekDayText.setTextColor(ContextCompat.getColor(requireContext(), tabItemColor))
                weekNumberDayText.setTextColor(ContextCompat.getColor(requireContext(), tabItemColor))
            }

            binding.weekTab.addOnTabSelectedListener(CalendarTabListener(requireContext()))
        }
    }
}