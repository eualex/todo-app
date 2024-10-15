package com.example.todoapp.presentation.screens.home

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
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private val viewModel by activityViewModel<HomeViewModel>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        setupViewPager()
        setupTabs()
        setupBottomAppBar()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadTaskGroup()
        viewModel.loadCategories()
    }

    private fun createBottomDialog(view: Int): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)

        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.BottomSheetDialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        return dialog
    }

    private fun setupTaskDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val taskDateCalendar = Calendar.getInstance()

                val taskDate = SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault()
                ).parse("$year-$month-$dayOfMonth")

                taskDateCalendar.set(Calendar.YEAR, year)
                taskDateCalendar.set(Calendar.MONTH, month)
                taskDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                viewModel.setTaskDate(taskDateCalendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    private fun setupCategoryDialog(onClose: () -> Unit) {
        val clCategoryModal = binding.clCategoryModalReference.clCategoryModal
        show(clCategoryModal)
        val cancelCardView: CardView = clCategoryModal.findViewById(R.id.cvCancelCategoryModal)
        val submitCardView: CardView = clCategoryModal.findViewById(R.id.cvSubmitCategoryModal)
        cancelCardView.setOnClickListener {
            hide(clCategoryModal)
            onClose()
        }
        submitCardView.setOnClickListener {
            hide(clCategoryModal)
            onClose()
        }

        val categoriesAdapter = CategoriesAdapter(viewModel::setCategory)
        val categoryListRecyclerView: RecyclerView =
            clCategoryModal.findViewById(R.id.rvCategoryList)
        categoryListRecyclerView.adapter = categoriesAdapter
        categoryListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val (categories) = viewModel.categories.value as CategoryResult.Success

        categoriesAdapter.setCategories(categories)

        clCategoryModal.findViewById<TextView>(R.id.tvEmptyCategories).visibility =
            if (categories.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }

        lifecycleScope.launch {
            viewModel.category.collect { category ->
                if (category != null) {
                    categoriesAdapter.setSelectedCategory(category)
                }
            }
        }
    }

    private fun setupAddTaskDialog() {
        val addTaskBottomDialog = createBottomDialog(R.layout.create_task_bottom_dialog)

        viewModel.setCategory(null)
        viewModel.setTaskDate(null)

        addTaskBottomDialog.show()

        val taskDateCardView: CardView = addTaskBottomDialog.findViewById(R.id.cvTaskDate)
        taskDateCardView.setOnClickListener {
            setupTaskDatePickerDialog()
        }

        val categoryCardView: CardView = addTaskBottomDialog.findViewById(R.id.cvTaskCategory)
        categoryCardView.setOnClickListener {
            addTaskBottomDialog.hide()
            setupCategoryDialog(
                onClose = addTaskBottomDialog::show
            )
        }

        val createTaskCardView: CardView = addTaskBottomDialog.findViewById(R.id.cvCreateTask)
        createTaskCardView.setOnClickListener {
            val taskTitle =
                addTaskBottomDialog.findViewById<TextView>(R.id.etTaskTitle).text.toString()
            viewModel.createTask(
                title = taskTitle,
            )
            viewModel.loadTaskGroup()
            addTaskBottomDialog.hide()
        }

        lifecycleScope.launch {
            viewModel.category.collect { category ->
                val (cardBackground, cardItemColor, cardText) = if (category != null) {
                    Triple(
                        ContextCompat.getColor(requireContext(), R.color.green_200),
                        ContextCompat.getColor(requireContext(), R.color.white),
                        category.title
                    )
                } else {
                    Triple(
                        ContextCompat.getColor(requireContext(), R.color.gray_250),
                        ContextCompat.getColor(requireContext(), R.color.gray_500),
                        "Categoria"
                    )
                }

                categoryCardView.setCardBackgroundColor(cardBackground)
                val categoryCardText = categoryCardView.findViewById<TextView>(R.id.tvTaskCategory)
                categoryCardText.text = cardText
                categoryCardText.setTextColor(cardItemColor)
                categoryCardView.findViewById<ImageView>(R.id.imgTaskCategory)
                    .setColorFilter(cardItemColor)
            }
        }

        lifecycleScope.launch {
            viewModel.taskDate.collect { taskDate ->
                val (cardBackground, cardItemColor, cardText) = if (taskDate != null) {
                    Triple(
                        ContextCompat.getColor(requireContext(), R.color.green_200),
                        ContextCompat.getColor(requireContext(), R.color.white),
                        SimpleDateFormat("dd/MM", Locale.getDefault()).format(taskDate)
                    )
                } else {
                    Triple(
                        ContextCompat.getColor(requireContext(), R.color.gray_250),
                        ContextCompat.getColor(requireContext(), R.color.gray_500),
                        "Hoje"
                    )
                }

                taskDateCardView.setCardBackgroundColor(cardBackground)
                val taskDateCardText = taskDateCardView.findViewById<TextView>(R.id.tvTaskDate)
                taskDateCardText.text = cardText
                taskDateCardText.setTextColor(cardItemColor)
                taskDateCardView.findViewById<ImageView>(R.id.imgTaskDate)
                    .setColorFilter(cardItemColor)
            }
        }
    }

    private fun setupMenuDialog() {
        val dialog = createBottomDialog(R.layout.menu_bottom_dialog)
        dialog.show()
        val configCardView = dialog.findViewById<CardView>(R.id.cvSettings)
        configCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
            dialog.hide()
        }

        val menuBottomCategoryAdapter = MenuBottomCategoriesAdapter()
        val menuCategoriesRecyclerView =
            dialog.findViewById<RecyclerView>(R.id.rvMenuCategoryList)
        val addCategoryCardView: CardView = dialog.findViewById(R.id.cvAddCategory)

        addCategoryCardView.setOnClickListener {
            dialog.hide()
            show(binding.clAddCategoryModalReference.clAddCategoryModal)
        }

        fun clearAndCloseAddCategoryModal() {
            hide(binding.clAddCategoryModalReference.clAddCategoryModal)
            dialog.show()
            binding.clAddCategoryModalReference.tietAddCategoryTitle.text?.clear()
        }

        binding.clAddCategoryModalReference.cvCancelAddCategoryModal.setOnClickListener {
            clearAndCloseAddCategoryModal()
        }

        binding.clAddCategoryModalReference.cvSubmitAddCategoryModal.setOnClickListener {
            val categoryTitle =
                binding.clAddCategoryModalReference.tietAddCategoryTitle.text.toString()
            if (categoryTitle.isNotEmpty()) {
                viewModel.createCategory(title = categoryTitle)
                viewModel.loadCategories()
                clearAndCloseAddCategoryModal()
            }
        }

        menuCategoriesRecyclerView.adapter = menuBottomCategoryAdapter
        menuCategoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.categories.collect { result ->
                when (result) {
                    is CategoryResult.Loading -> {}
                    is CategoryResult.Error -> {}
                    is CategoryResult.Success -> {
                        menuBottomCategoryAdapter.setCategories(result.categories)

                        dialog.findViewById<View>(R.id.llCategoryWrapper).visibility =
                            if (result.categories.isEmpty()) {
                                View.GONE
                            } else {
                                View.VISIBLE
                            }
                    }
                }
            }
        }
    }

    private fun setupBottomAppBar() {
        binding.cvAddTask.setOnClickListener {
            setupAddTaskDialog()
        }

        binding.cvMenu.setOnClickListener {
            setupMenuDialog()
        }

        binding.cvSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun hide(view: View) {
        view.visibility = View.GONE
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
//                viewModel.filterTaskList(viewModel.weekDays[nextTabIndex].dayNumber)
            }
        }
    }
}