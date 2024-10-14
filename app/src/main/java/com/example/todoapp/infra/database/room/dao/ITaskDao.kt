package com.example.todoapp.infra.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.todoapp.infra.database.room.entities.TaskEntity
import com.example.todoapp.infra.database.room.entities.TaskWithCategory

@Dao
interface ITaskDao {

    @Transaction
    @Query("SELECT * FROM task")
    suspend fun findAll(): List<TaskWithCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(task: TaskEntity)
}