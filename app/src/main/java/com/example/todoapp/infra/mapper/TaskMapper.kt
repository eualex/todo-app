package com.example.todoapp.infra.mapper

import com.example.todoapp.domain.model.Task
import com.example.todoapp.infra.database.room.entities.TaskEntity

fun Task.toDatabase() = TaskEntity(
    title = this.title,
    date = this.date.time,
    finished = this.finished,
    taskCategoryId = this.category.id
)