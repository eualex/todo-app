package com.example.todoapp.presentation.screen.home.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatDate(pattern: String, date: Calendar): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date.time)
}

fun isSameDay(calendar1: Calendar, calendar2: Calendar): Boolean {
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

fun getWeekDaysWithDates(): List<WeekDay> {
    val today = Calendar.getInstance()
    val monday = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    return (0..6).map { i ->
        val date = (monday.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, i)
        }
        val weekDay = formatDate("E", date)
        val weekNumberDay = formatDate("dd", date)
        val isDisabled = date.before(today)
        val isCurrentDay = isSameDay(date, today)

        WeekDay(weekDay, weekNumberDay, isDisabled, isCurrentDay)
    }
}