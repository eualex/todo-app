package com.example.todoapp.presentation.screen.search;

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.domain.model.Category
import com.example.todoapp.domain.model.Task

class SearchResultListAdapter(
    private var searchResult: SearchResult
) : RecyclerView.Adapter<SearchResultListAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindCategory(category: Category) {
            view.findViewById<TextView>(R.id.tvSearchResultTitle).text = category.title
            view.findViewById<TextView>(R.id.tvSearchResultTaskCount).text =
                category.taskList.size.toString()
        }

        fun bindTask(task: Task) {
            view.findViewById<TextView>(R.id.taskTitle).text = task.title
            view.findViewById<TextView>(R.id.taskCategory).text =
                task.category.title

            view.findViewById<CardView>(R.id.taskCard).setOnClickListener { view ->
                val checkbox = view.findViewById<CheckBox>(R.id.taskCheckbox)
                checkbox.isChecked = !checkbox.isChecked
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSearchResult(result: SearchResult) {
        searchResult = result
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = when (viewType) {
            VIEW_TYPE_TASK -> layoutInflater.inflate(
                R.layout.task_list_item,
                parent,
                false
            )
            VIEW_TYPE_CATEGORY -> layoutInflater.inflate(
                R.layout.category_search_result,
                parent,
                false
            )
            // TODO: create a loading item
            VIEW_TYPE_LOADING -> layoutInflater.inflate(
                R.layout.task_list_item,
                parent,
                false
            )
            else -> layoutInflater.inflate(
                R.layout.task_list_item,
                parent,
                false
            )
        }

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = when (searchResult) {
        is SearchResult.TaskResult -> (searchResult as SearchResult.TaskResult).taskList.size
        is SearchResult.CategoryResult -> (searchResult as SearchResult.CategoryResult).categoryList.size
        is SearchResult.Loading -> 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (searchResult) {
            is SearchResult.CategoryResult -> {
                holder.bindCategory((searchResult as SearchResult.CategoryResult).categoryList[position])
            }

            is SearchResult.TaskResult -> {
                holder.bindTask((searchResult as SearchResult.TaskResult).taskList[position])
            }

            is SearchResult.Loading -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (searchResult) {
            is SearchResult.Loading -> VIEW_TYPE_LOADING
            is SearchResult.TaskResult -> VIEW_TYPE_TASK
            is SearchResult.CategoryResult -> VIEW_TYPE_CATEGORY
        }
    }

    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_TASK = 1
        const val VIEW_TYPE_CATEGORY = 2
    }
}
