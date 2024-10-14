package com.example.todoapp.infra.database.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithCategory(
    @Embedded
    val task: TaskEntity,

    @Relation(
        parentColumn = "taskCategoryId",
        entityColumn = "categoryId"
    )
    val category: CategoryEntity
)