package com.example.todoapp.domain.model

import java.util.Calendar

data class WeekDay(
    val shortDayText: String,
    val dayText: String,
    val dayNumber: String,
    val isDisabled: Boolean,
    val isCurrentDay: Boolean,
    val isTomorrow: Boolean,
    val date: Calendar
)
