package com.example.todoapp.infra.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.todoapp.infra.database.room.entities.CategoryEntity
import com.example.todoapp.infra.database.room.entities.CategoryWithTaskList

@Dao
interface ICategoryDao {

    @Transaction
    @Query("SELECT * FROM category")
    suspend fun findAll(): List<CategoryWithTaskList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(category: CategoryEntity)
}