package com.example.todoapp.presentation.screens.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.todoapp.R
import com.example.todoapp.domain.model.Category

class CategoriesAdapter(val onSelectCategory: (Category) -> Unit) :
    Adapter<CategoriesAdapter.ViewHolder>() {

    private var categories: List<Category> = emptyList()
    private var selectedCategory: Category? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(categoryList: List<Category>) {
        categories = categoryList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedCategory(category: Category?) {

        selectedCategory = category
        // TODO: implement better deactivation of current active item
        notifyDataSetChanged()
//        notifyItemChanged(categories.indexOf(category))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.category_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(category = categories[position])
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(category: Category) {
            val categoryCardView: CardView = view.findViewById(R.id.cvCategoryItem)
            val categoryTitleTextView: TextView = view.findViewById(R.id.tvCategoryItemTitle)
            val categoryTaskCountTextView: TextView =
                view.findViewById(R.id.tvCategoryItemTaskCount)

            val isActiveCategory = selectedCategory != null && selectedCategory == category
            val cardForeground = ContextCompat.getDrawable(
                view.context,
                if (isActiveCategory) {
                    R.drawable.category_item_outline_active
                } else {
                    R.drawable.category_item_outline
                }
            )
            val cardItemColor = ContextCompat.getColor(
                view.context, if (isActiveCategory) {
                    R.color.green_200
                } else {
                    R.color.gray_500
                }
            )

            categoryCardView.setOnClickListener {
                onSelectCategory(category)
            }

            categoryCardView.foreground = cardForeground
            categoryTitleTextView.setTextColor(cardItemColor)
            categoryTaskCountTextView.setTextColor(cardItemColor)

            categoryTitleTextView.text = category.title
            categoryTaskCountTextView.text = "${category.taskList.size} Tarefas"
        }
    }
}