package com.example.todoapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.example.todoapp.domain.model.Category
import com.example.todoapp.domain.model.Task
import com.example.todoapp.domain.model.TaskGroup
import com.example.todoapp.presentation.screens.home.helpers.getWeekDaysWithDates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class HomeViewModel : ViewModel() {
    val weekDays = getWeekDaysWithDates()
    private val taskList = weekDays.map { weekDay ->
        TaskGroup(
            weekDay,
            items = listOf(
                Task(
                    "Limpar a casa",
                    Category(
                        "Pessoal",
                        emptyList()
                    )
                ),
                Task(
                    "Finalizar tela",
                    Category(
                        "Trabalho",
                        emptyList()
                    )
                ),
            )
        )
    }
    private val _filteredTaskList = MutableStateFlow(taskList.toList())
    val filteredTaskListGroup = _filteredTaskList.asStateFlow()

    fun filterTaskList(selectedDay: String) {
        val targetDay = weekDays.find {
            it.dayNumber == selectedDay
        }

        if (targetDay == null) return

        _filteredTaskList.value = taskList.filter {
            targetDay.date.before((it.weekDay.date.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, 1)
            })
        }
    }
}