package com.example.todoapp.infra.mapper

import com.example.todoapp.domain.model.Category
import com.example.todoapp.infra.database.room.entities.CategoryEntity

fun Category.toDatabase() = CategoryEntity(name = this.title)