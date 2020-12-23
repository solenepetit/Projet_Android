package com.example.projetandroid.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projetandroid.tasklist.Task

/**
 * DatabaseTasks represents a task entity in the database
 */
@Entity
data class DatabaseTasks (
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String)

/**
 * Map DatabaseTasks to domain entities
 */
fun List<DatabaseTasks>.asDomainModel() : List<Task> {
    return map{
        Task(
            id = it.id,
            title = it.title,
            description = it.description
        )
    }
}