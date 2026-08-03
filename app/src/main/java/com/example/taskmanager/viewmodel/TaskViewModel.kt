package com.example.taskmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.model.Task
import kotlinx.coroutines.launch

class TaskViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val taskDao =
        TaskDatabase.getDatabase(application).taskDao()

    val allTasks: LiveData<List<Task>> =
        taskDao.getAllTasks()

    fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }
    suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId)
    }
}