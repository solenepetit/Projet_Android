package com.example.projetandroid.tasklist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projetandroid.database.DatabaseTasks
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task (
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String = "TODO"
    ) : java.io.Serializable

fun List<Task>.asDatabaseModel() : List<DatabaseTasks> {
    return map {
        DatabaseTasks(
            id = it.id,
            title = it.title,
            description = it.description)
    }
}