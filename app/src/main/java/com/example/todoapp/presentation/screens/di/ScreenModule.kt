package com.example.todoapp.presentation.screens.di

import com.example.todoapp.presentation.screens.home.HomeViewModel
import com.example.todoapp.presentation.screens.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val screenModule = module {
    viewModel { HomeViewModel() }
    viewModel { SearchViewModel() }
}