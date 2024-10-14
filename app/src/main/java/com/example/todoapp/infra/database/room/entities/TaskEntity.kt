package com.example.todoapp.infra.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "task"
//    foreignKeys = [
//        ForeignKey(
//            entity = Category::class,
//            parentColumns = ["categoryId"],
//            childColumns = ["taskCategoryId"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ],
//    indices = [
//        Index("taskCategoryId")
//    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val taskId: Long = 0L,
    val title: String,
    val finished: Boolean,
    val taskCategoryId: Long,
    val date: Long
)