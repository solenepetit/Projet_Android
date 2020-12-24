package com.example.projetandroid.tasklist

import android.app.Application
import androidx.lifecycle.*
import com.example.projetandroid.database.getDatabase
import com.example.projetandroid.network.TasksRepository
import kotlinx.coroutines.launch

class TaskListViewModel(application : Application): AndroidViewModel(application) {
    /*
    init {
        viewModelScope.launch {
            refresh()
        }
    }*/
    private val repository = TasksRepository(getDatabase(application))
    //private val _taskList = MutableLiveData<List<Task>>()
    //val taskList: LiveData<List<Task>> = _taskList
    val taskList = repository.tasks
    suspend fun refresh() {repository.refresh()}
    suspend fun deleteTask(task: Task) {repository.deleteTask(task)}
    suspend fun addTask(task: Task) {repository.addTask(task)}
    suspend fun updateTask(task: Task) {repository.updateTask(task)}

    /**
     * Factory for constructing TaskListViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TaskListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

