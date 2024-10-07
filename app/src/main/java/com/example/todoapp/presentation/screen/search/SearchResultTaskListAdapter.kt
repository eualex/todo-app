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

class SearchResultTaskListAdapter(
    private var taskList: List<Task>
) : RecyclerView.Adapter<SearchResultTaskListAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(task: Task) {
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
    fun setTaskList(newTaskList: List<Task>) {
        taskList = newTaskList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.task_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(task = taskList[position])
    }
}
