package com.example.projetandroid.network

import com.example.projetandroid.tasklist.Task
import retrofit2.Response
import retrofit2.http.GET

interface TasksWebService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>
}