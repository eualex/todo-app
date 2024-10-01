package com.example.todoapp.presentation.screen.home

import TaskListFragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.domain.model.Task
import com.example.todoapp.domain.model.TaskGroup
import com.example.todoapp.presentation.screen.home.helpers.getWeekDaysWithDates
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val weekDays = getWeekDaysWithDates()

    private val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) LocalDate.now()
        .toString() else SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    private val taskList = listOf(
        TaskGroup(
            date = date,
            items = listOf(
                Task(
                    "Limpar a casa",
                    "Pessoal"
                ),
                Task(
                    "Finalizar tela",
                    "Trabalho"
                ),
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.taskViewPager.adapter = object : FragmentStateAdapter(requireActivity()) {
            override fun getItemCount(): Int = weekDays.size

            override fun createFragment(position: Int): Fragment {
                return TaskListFragment(taskList)
            }
        }

        TabLayoutMediator(binding.weekTab, binding.taskViewPager) { tab, position ->
            setupCustomTabs(tab, position)
        }.attach()
        setupTabListener()
        setupTabState()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeTabColor(tab: TabLayout.Tab?) {
        if (tab == null) return

        val weekNumberDayText = tab.view.findViewById<TextView>(R.id.weekNumberDayText)
        val weekDayText = tab.view.findViewById<TextView>(R.id.weekDayText)

        val tabItemColorRes =
            if (!tab.view.isEnabled) R.color.gray_350 else if (tab.isSelected) R.color.green_200 else R.color.black
        val tabItemColor = ContextCompat.getColor(requireContext(), tabItemColorRes)

        weekDayText.setTextColor(tabItemColor)
        weekNumberDayText.setTextColor(tabItemColor)
        binding.weekTab.setSelectedTabIndicatorColor(tabItemColor)
    }

    private fun setupTabListener() {
        binding.weekTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeTabColor(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                changeTabColor(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                changeTabColor(tab)
            }
        })
    }

    private fun setupCustomTabs(tab: TabLayout.Tab, position: Int) {
        val (weekDay, weekDayNumber, isWeekDayDisabled) = weekDays[position]

        val tabItemView = layoutInflater.inflate(R.layout.tab_icon_item, null)
        val weekNumberDayText = tabItemView.findViewById<TextView>(R.id.weekNumberDayText)
        val weekDayText = tabItemView.findViewById<TextView>(R.id.weekDayText)

        weekDayText.text = weekDay
        weekNumberDayText.text = weekDayNumber
        tab.customView = tabItemView
        tab.view.isEnabled = !isWeekDayDisabled

        changeTabColor(tab)
    }

    private fun setupTabState() {
        weekDays.forEachIndexed { index, weekDay ->
            if (weekDay.isDisabled) {
                binding.taskViewPager.setCurrentItem(index + 1, false)
            }
        }
    }
}