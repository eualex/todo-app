package com.example.todoapp.infra.di

import com.example.todoapp.infra.repository.CategoryRepository
import com.example.todoapp.infra.repository.TaskRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { CategoryRepository(get()) }
    single { TaskRepository(get()) }
}