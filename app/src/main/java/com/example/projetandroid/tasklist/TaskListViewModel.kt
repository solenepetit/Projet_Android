package com.example.projetandroid.tasklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetandroid.database.getDatabase
import com.example.projetandroid.network.TasksRepository
import okhttp3.MultipartBody

class TaskListViewModel(application : Application): AndroidViewModel(application) {
    private val repository = TasksRepository(getDatabase(application))
    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {//repository.refresh()
        val tasksResponse = repository.tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            _taskList.value = fetchedTasks!!
        }
        else null}
    suspend fun deleteTask(task: Task) {repository.deleteTask(task)}
    suspend fun addTask(task: Task) {repository.addTask(task)}
    suspend fun updateTask(task: Task) {repository.updateTask(task)}

}