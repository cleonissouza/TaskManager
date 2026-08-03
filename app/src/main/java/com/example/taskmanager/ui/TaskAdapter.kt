package com.example.taskmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.ItemTaskBinding
import com.example.taskmanager.model.Task
class TaskAdapter(
    private var taskList: List<Task>,
    private val onTaskClick: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.textViewTaskTitle.text = task.title
            binding.textViewTaskDate.text = "Data: ${task.date}"
            binding.textViewTaskPriority.text = "Prioridade: ${task.priority}"
            binding.textViewTaskStatus.text = "Status: ${task.status}"

            binding.root.setOnClickListener {
                onTaskClick(task)
            }

            binding.root.setOnLongClickListener {
                onDeleteClick(task)
                true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {

        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun updateTasks(newTaskList: List<Task>) {
        taskList = newTaskList
        notifyDataSetChanged()
    }
}