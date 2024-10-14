package com.example.todoapp.domain.model;

data class Category(
    val id: Long = 0L,
    val title: String,
    var taskList: List<Task> = emptyList()
)
