package com.example.todoapp.infra.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.infra.database.room.dao.ICategoryDao
import com.example.todoapp.infra.database.room.dao.ITaskDao
import com.example.todoapp.infra.database.room.entities.CategoryEntity
import com.example.todoapp.infra.database.room.entities.TaskEntity

@Database(
    entities = [CategoryEntity::class, TaskEntity::class],
    version = 3,
    exportSchema = false
)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun taskDao(): ITaskDao
    abstract fun categoryDao(): ICategoryDao

    companion object {
        @Volatile
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            return instance ?: synchronized(this) {
               val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_db"
                ).fallbackToDestructiveMigration().build()
                instance = newInstance
                newInstance
            }
        }
    }
}