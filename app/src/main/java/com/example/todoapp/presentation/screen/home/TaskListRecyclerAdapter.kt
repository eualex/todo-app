package com.example.todoapp.presentation.screen.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.R
import com.example.todoapp.domain.model.Task
import com.example.todoapp.domain.model.TaskGroup

class TaskListRecyclerAdapter(private val taskList: List<TaskGroup>): Adapter<TaskListRecyclerAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(private val view: View): ViewHolder(view) {
        fun bind(task: Task) {
            val taskTitle = view.findViewById<TextView>(R.id.taskTitle)
            val taskCard = view.findViewById<CardView>(R.id.taskCard)
            val taskCategory = view.findViewById<TextView>(R.id.taskCategory)
            taskTitle.text = task.title
            taskCategory.text = task.category.toString().replaceFirstChar { it.uppercaseChar() }

            taskCard.setOnClickListener { view ->
                val checkbox = view.findViewById<CheckBox>(R.id.taskCheckbox)
                checkbox.isChecked = !checkbox.isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            R.layout.task_list_item,
            parent,
            false
        )

        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList.first().items[position])
    }

    override fun getItemCount(): Int = taskList.first().items.size
}