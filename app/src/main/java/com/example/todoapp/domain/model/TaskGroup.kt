package com.example.todoapp.domain.model

data class TaskGroup(
    val weekDay: WeekDay,
    val items: List<Task>
)