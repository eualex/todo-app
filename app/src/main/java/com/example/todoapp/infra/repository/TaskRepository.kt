package com.example.todoapp.infra.repository

import com.example.todoapp.domain.model.Category
import com.example.todoapp.domain.model.Task
import com.example.todoapp.infra.database.room.dao.ITaskDao
import com.example.todoapp.infra.mapper.toDatabase
import java.util.Date

class TaskRepository(private val taskDao: ITaskDao) {
    suspend fun findAll(): List<Task> {
        return taskDao.findAll().map { rawTask ->
            val task = Task(
                title = rawTask.task.title,
                id = rawTask.task.taskId,
                finished = rawTask.task.finished,
                date = Date(rawTask.task.date),
                category = Category(
                    id = rawTask.category.categoryId,
                    title = rawTask.category.name,

                    // No need to find all tasks of this category, but it can be done with categoryDao
                    taskList = emptyList()
                )
            )

            task
        }
    }

    suspend fun create(task: Task) {
        taskDao.create(task.toDatabase())
    }
}