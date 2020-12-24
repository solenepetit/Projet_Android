package com.example.projetandroid.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.projetandroid.database.TasksDatabase
import com.example.projetandroid.database.asDomainModel
import com.example.projetandroid.tasklist.Task
import com.example.projetandroid.tasklist.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TasksRepository(private val database : TasksDatabase) {
    val tasksWebService = Api.INSTANCE.tasksWebService
    val tasks : LiveData<List<Task>> = Transformations.map(database.taskDao.getTasks()) {
        it.asDomainModel()
    }

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            // Call HTTP (opération longue):
            val tasksResponse = tasksWebService.getTasks()
            // À la ligne suivante, on a reçu la réponse de l'API:
            if (tasksResponse.isSuccessful) {
                database.taskDao.deleteAll()
                database.taskDao.insertAll(tasksResponse.body()!!.asDatabaseModel())
                //tasksResponse.body()
            }
            else null
        }
    }

    suspend fun addTask(task : Task) {

        tasksWebService.createTask(task)

    }
    suspend fun updateTask(task : Task) {

        tasksWebService.updateTask(task)

    }

    suspend fun deleteTask(task: Task) {
        tasksWebService.deleteTask(task.id)
    }


}