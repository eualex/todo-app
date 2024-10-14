package com.example.todoapp.infra.repository

import com.example.todoapp.domain.model.Category
import com.example.todoapp.domain.model.Task
import com.example.todoapp.infra.database.room.dao.ICategoryDao
import com.example.todoapp.infra.mapper.toDatabase
import java.util.Date

class CategoryRepository(private val categoryDao: ICategoryDao) {
    suspend fun create(category: Category) {
        categoryDao.create(category.toDatabase())
    }

    suspend fun findAll(): List<Category> {
        return categoryDao.findAll().map { rawCategory ->
            val category = Category(
                title = rawCategory.category.name,
                taskList = emptyList(),
                id = rawCategory.category.categoryId
            )
            val taskList = rawCategory.taskList.map { rawTask -> Task(
                title = rawTask.title,
                category = category,
                finished = rawTask.finished,
                date = Date(rawTask.date),
                id = rawTask.taskId
            ) }
            category.taskList = taskList
            category
        }
    }
}
