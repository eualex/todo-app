package com.example.todoapp.presentation.screens.home.helpers

import android.util.Log
import com.example.todoapp.domain.model.WeekDay
import java.text.SimpleDateFormat
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

fun formatDate(pattern: String, date: Calendar): String {
    val formatter = SimpleDateFormat(pattern, Locale("pt", "BR"))
    return formatter.format(date.time)
}

fun isSameDay(calendar1: Calendar, calendar2: Calendar): Boolean {
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

fun dateToCalendar(date: Date): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}

fun getWeekDaysWithDates(): List<WeekDay> {
    val today = Calendar.getInstance()
    val monday = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }
    val tomorrow = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, 1) }

    return (0..6).map { i ->
        val date = (monday.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, i)
        }
        val shortWeekDay = formatDate("E", date).replaceFirstChar { it.uppercaseChar() }
        val weekDay = formatDate("EEEE", date).replaceFirstChar { it.uppercaseChar() }
        val weekNumberDay = formatDate("dd", date)
        val isDisabled = date.before(today)
        val isTomorrow = isSameDay(date, tomorrow)
        val isCurrentDay = isSameDay(date, today)

        WeekDay(shortWeekDay, weekDay, weekNumberDay, isDisabled, isCurrentDay, isTomorrow, date)
    }
}