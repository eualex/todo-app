package com.example.todoapp.domain.model

import java.util.Date

data class Task(
    val id: Long = 0L,
    val title: String,
    val date: Date,
    val finished: Boolean,
    val category: Category
)