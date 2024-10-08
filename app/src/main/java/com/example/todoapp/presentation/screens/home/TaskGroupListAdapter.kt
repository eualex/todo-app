package com.example.todoapp.presentation.screens.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.todoapp.R
import com.example.todoapp.domain.model.TaskGroup

class TaskGroupListAdapter(private var taskGroupList: List<TaskGroup>) :
    Adapter<TaskGroupListAdapter.ViewHolder>() {
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(taskGroup: TaskGroup) {
            val weekContainer = view.findViewById<LinearLayout>(R.id.tvWeekDayContainer)

            weekContainer.findViewById<TextView>(R.id.tvWeekDayNumberListItem).text =
                if (taskGroup.weekDay.isCurrentDay) "Hoje" else if (taskGroup.weekDay.isTomorrow) "Amanhã" else taskGroup.weekDay.dayNumber
            weekContainer.findViewById<TextView>(R.id.tvWeekDayNameListItem).text =
                taskGroup.weekDay.dayText

            val recyclerView = view.findViewById<RecyclerView>(R.id.rvTaskList)
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            recyclerView.adapter = TaskListRecyclerAdapter(taskGroup.items)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(
            R.layout.task_group_list_item,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount() = taskGroupList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(taskGroupList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTaskListGroup(newTaskListGroup: List<TaskGroup>) {
        taskGroupList = newTaskListGroup
        notifyDataSetChanged()
    }
}