package com.example.todoapp.presentation.screens.home

import android.util.AndroidRuntimeException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.model.Category
import com.example.todoapp.domain.model.Task
import com.example.todoapp.domain.model.TaskGroup
import com.example.todoapp.infra.database.room.dao.ITaskDao
import com.example.todoapp.infra.repository.CategoryRepository
import com.example.todoapp.infra.repository.TaskRepository
import com.example.todoapp.presentation.screens.home.helpers.dateToCalendar
import com.example.todoapp.presentation.screens.home.helpers.getWeekDaysWithDates
import com.example.todoapp.presentation.screens.home.helpers.isSameDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

sealed class TaskGroupResult {
    data class Success(val taskGroupList: List<TaskGroup>): TaskGroupResult()
    data class Error(val message: String): TaskGroupResult()
    data object Loading: TaskGroupResult()
}

sealed class CategoryResult {
    data class Success(val categories: List<Category>): CategoryResult()
    data class Error(val message: String): CategoryResult()
    data object Loading: CategoryResult()
}

class HomeViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
): ViewModel() {
    val weekDays = getWeekDaysWithDates()

    private val _taskGroup = MutableStateFlow<TaskGroupResult>(TaskGroupResult.Loading)
    val taskGroup = _taskGroup.asStateFlow()

    private val _filteredTaskList = MutableStateFlow<List<TaskGroup>>(emptyList())
    val filteredTaskListGroup = _filteredTaskList.asStateFlow()

    private val _categories = MutableStateFlow<CategoryResult>(CategoryResult.Loading)
    val categories = _categories.asStateFlow()

    private val _category = MutableStateFlow<Category?>(null)
    val category = _category.asStateFlow()

    private val _taskDate = MutableStateFlow<Date?>(null)
    val taskDate = _taskDate.asStateFlow()

    fun filterTaskList(selectedDay: String? = null) {
        val targetDay = weekDays.find {
            it.dayNumber == selectedDay
        }

        if (targetDay == null) return

        _filteredTaskList.value = when (_taskGroup.value) {
            is TaskGroupResult.Loading -> emptyList()
            is TaskGroupResult.Success -> (_taskGroup.value as TaskGroupResult.Success).taskGroupList.filter {
                targetDay.date.before((it.weekDay.date.clone() as Calendar).apply {
                    add(Calendar.DAY_OF_MONTH, 1)
                })
            }
            is TaskGroupResult.Error -> emptyList()
        }
    }

    fun loadTaskGroup() {
        viewModelScope.launch {
            val tasks = taskRepository.findAll()
            val taskGroupSuccess = TaskGroupResult.Success(weekDays.mapNotNull { weekDay ->
                val taskOfDayList =
                    tasks.filter {
                        val res = isSameDay(weekDay.date, dateToCalendar(it.date))
                        res
                    }
                if (taskOfDayList.isEmpty()) {
                    null
                } else {
                    TaskGroup(
                        weekDay = weekDay,
                        items = taskOfDayList
                    )
                }
            })

            _taskGroup.value = taskGroupSuccess
        }
    }

    fun createCategory(title: String) {
        viewModelScope.launch {
            //TODO: use either pattern to handle success or error
            categoryRepository.create(
                Category(
                    title = title,
                )
            )
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            val categories = categoryRepository.findAll()
            _categories.value = CategoryResult.Success(categories)
        }
    }

    fun setCategory(category: Category?) {
        _category.value = category
    }

    fun setTaskDate(date: Date?) {
        _taskDate.value = date
    }

    fun createTask(title: String) {
        if(_category.value == null || _taskDate.value == null) return
        // TODO: add either
        viewModelScope.launch {
            taskRepository.create(
                task = Task(
                    title = title,
                    category = _category.value as Category,
                    date = _taskDate.value as Date,
                    finished = false,
                )
            )
        }
    }
}