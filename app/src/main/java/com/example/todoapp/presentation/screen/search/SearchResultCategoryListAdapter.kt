package com.example.todoapp.presentation.screen.search;

import android.annotation.SuppressLint
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

class SearchResultCategoryListAdapter(
    private var categoryList: List<Category>
) : RecyclerView.Adapter<SearchResultCategoryListAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(category: Category) {
            view.findViewById<TextView>(R.id.tvSearchResultTitle).text = category.title
            view.findViewById<TextView>(R.id.tvSearchResultTaskCount).text =
                category.taskList.size.toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryList(newCategoryList: List<Category>) {
        categoryList = newCategoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.category_search_result, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }
}
