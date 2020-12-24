package com.example.projetandroid.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetandroid.network.TasksRepository

class TaskListViewModel: ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList
    suspend fun refresh() {repository.refresh()}
    suspend fun deleteTask(task: Task) {repository.deleteTask(task)}
    suspend fun addTask(task: Task) {repository.addTask(task)}
    suspend fun updateTask(task: Task) {repository.updateTask(task)}

}