package com.example.todoapp.infra.database.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithTaskList (
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "taskCategoryId"
    )
    val taskList: List<TaskEntity>
)