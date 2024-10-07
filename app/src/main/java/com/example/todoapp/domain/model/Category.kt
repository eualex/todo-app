package com.example.todoapp.domain.model;

data class Category(
    val title: String,
    val taskList: List<Task>
)
