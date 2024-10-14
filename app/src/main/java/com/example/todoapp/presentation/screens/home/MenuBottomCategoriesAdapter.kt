package com.example.todoapp.presentation.screens.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.todoapp.R
import com.example.todoapp.domain.model.Category

class MenuBottomCategoriesAdapter : Adapter<MenuBottomCategoriesAdapter.ViewHolder>() {

    private var categories: List<Category> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(categoryList: List<Category>) {
        categories = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.menu_bottom_category_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(category = categories[position])
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(category: Category) {
            val categoryTitleTextView: TextView = view.findViewById(R.id.tvMenuCategoryTitle)
            val categoryTaskCountTextView: TextView =
                view.findViewById(R.id.tvMenuCategoryTaskCount)

            categoryTitleTextView.text = category.title
            categoryTaskCountTextView.text = "${category.taskList.size} Tarefas"
        }
    }
}