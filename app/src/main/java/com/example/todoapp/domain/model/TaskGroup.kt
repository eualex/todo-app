package com.example.todoapp.domain.model

data class TaskGroup(
    val date: String,
    val items: List<Task>
)