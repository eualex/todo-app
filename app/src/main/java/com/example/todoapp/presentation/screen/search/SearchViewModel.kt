package com.example.todoapp.presentation.screen.search

import androidx.lifecycle.ViewModel
import com.example.todoapp.domain.model.Category
import com.example.todoapp.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class FilterType {
    data object Empty: FilterType()
    data object Task: FilterType()
    data object Category: FilterType()
}

sealed class SearchResult {
    data class TaskResult(val taskList: List<Task>): SearchResult()
    data class CategoryResult(val categoryList: List<Category>): SearchResult()
    data object Loading: SearchResult()
}

class SearchViewModel: ViewModel() {
    private val _filterBy = MutableStateFlow<FilterType>(FilterType.Empty)
    val filterBy = _filterBy.asStateFlow()

    private val _result = MutableStateFlow<SearchResult>(SearchResult.Loading)
    val result = _result.asStateFlow()

    fun toggleResult() {
        _result.value = when(filterBy.value) {
            FilterType.Category -> SearchResult.CategoryResult(
                listOf(
                    Category(
                        "Pessoal",
                        emptyList()
                    ),
                    Category(
                        "Trabalho",
                        emptyList()
                    ),
                    Category(
                        "Hobby",
                        emptyList()
                    ),
                ),
            )

            FilterType.Task -> SearchResult.TaskResult(
                listOf(
                    Task(
                        "Fazer tela",
                        Category(
                            "Trabalho",
                            emptyList()
                        )
                    ),
                    Task(
                        "Musculação",
                        Category(
                            "Hobby",
                            emptyList()
                        )
                    )
                )
            )

            FilterType.Empty -> SearchResult.Loading
        }
    }

    fun setFilterType(filterType: FilterType) {
        _filterBy.value = filterType
    }
}