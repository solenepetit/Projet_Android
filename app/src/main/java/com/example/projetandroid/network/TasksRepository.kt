package com.example.projetandroid.network

import com.example.projetandroid.tasklist.Task
import okhttp3.MultipartBody

class TasksRepository {
    val tasksWebService = Api.tasksWebService

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            tasksResponse.body()
        }
        else null
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

    suspend fun updateAvatar(avatar: MultipartBody.Part) {
        tasksWebService.updateAvatar(avatar)
    }
}