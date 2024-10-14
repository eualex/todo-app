package com.example.todoapp.infra.database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0L,
    val name: String,
)