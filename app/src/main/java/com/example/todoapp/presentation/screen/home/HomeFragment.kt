package com.example.todoapp.presentation.screen.home

import TaskListFragment
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Calendar

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        setupViewPager()
        setupTabs()
        setupBottomAppBar()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomAppBar() {

        fun createDialog(view: Int): Dialog {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(view)

            dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.BottomSheetDialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)

            return dialog
        }

        fun openTaskDatePickerDialog() {
            val calendar = Calendar.getInstance()

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            val startOfWeek = calendar.timeInMillis

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
            val endOfWeek = calendar.timeInMillis

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, month, dayOfMonth ->

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.minDate = startOfWeek
            datePickerDialog.datePicker.maxDate = endOfWeek

            datePickerDialog.show()
        }

        binding.cvAddTask.setOnClickListener {
            val dialog = createDialog(R.layout.create_task_bottom_dialog)
            dialog.show()
            val taskDateCardView: CardView = dialog.findViewById(R.id.cvTaskDate)
            taskDateCardView.setOnClickListener {
                openTaskDatePickerDialog()
            }
        }

        binding.cvMenu.setOnClickListener {
            val dialog = createDialog(R.layout.menu_bottom_dialog)
            dialog.show()
            val configCardView = dialog.findViewById<CardView>(R.id.cvSettings)
            configCardView.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_homeFragment_to_settingsFragment)
                dialog.hide()
            }
        }

        binding.cvSearch.setOnClickListener {
            findNavController()
                .navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun setupViewPager() {
        binding.taskViewPager.adapter = object : FragmentStateAdapter(requireActivity()) {
            override fun getItemCount(): Int = viewModel.weekDays.size

            override fun createFragment(position: Int): Fragment {
                return TaskListFragment()
            }
        }

        binding.taskViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentWeekDay = viewModel.weekDays[position]
                viewModel.filterTaskList(currentWeekDay.dayNumber)
            }
        })
    }

    private fun setupTabs() {
        TabLayoutMediator(binding.weekTab, binding.taskViewPager) { tab, position ->
            setupCustomTabs(tab, position)
        }.attach()
        setupTabListener()
        setupTabState()
    }

    private fun changeTabColor(tab: TabLayout.Tab?) {
        if (tab == null) return

        val weekNumberDayText = tab.view.findViewById<TextView>(R.id.tvWeekNumberDay)
        val weekDayText = tab.view.findViewById<TextView>(R.id.tvWeekDay)

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
        val weekDay = viewModel.weekDays[position]

        val tabItemView = layoutInflater.inflate(R.layout.tab_icon_item, null)
        val weekNumberDayText = tabItemView.findViewById<TextView>(R.id.tvWeekNumberDay)
        val weekDayText = tabItemView.findViewById<TextView>(R.id.tvWeekDay)

        weekDayText.text = weekDay.shortDayText
        weekNumberDayText.text = weekDay.dayNumber
        tab.customView = tabItemView
        tab.view.isEnabled = !weekDay.isDisabled

        changeTabColor(tab)
    }

    private fun setupTabState() {
        viewModel.weekDays.forEachIndexed { index, weekDay ->
            if (weekDay.isDisabled) {
                val nextTabIndex = index + 1
                binding.taskViewPager.setCurrentItem(nextTabIndex, false)
            }
        }
    }
}