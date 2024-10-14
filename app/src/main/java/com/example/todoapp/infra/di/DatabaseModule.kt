package com.example.todoapp.infra.di

import com.example.todoapp.infra.database.room.TodoDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val todoDatabaseDao = module {
    single { TodoDatabase.getInstance(androidContext()).taskDao() }
    single { TodoDatabase.getInstance(androidContext()).categoryDao() }
}