package com.example.projetandroid.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projetandroid.network.UserInfo
import com.example.projetandroid.tasklist.Task

/**
 * DatabaseTasks represents a task entity in the database
 */
@Entity
data class DatabaseTask (
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String)

@Entity
data class DatabaseUserInfo(
        @PrimaryKey
        val email : String,
        val firstName: String,
        val lastName: String,
        val avatar : String
)

/**
 * Map DatabaseTasks to domain entities
 */
fun List<DatabaseTask>.asDomainModel() : List<Task> {
    return map{
        Task(
            id = it.id,
            title = it.title,
            description = it.description
        )
    }
}

fun DatabaseUserInfo.asUserDomainModel() : UserInfo {
    return UserInfo(
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            avatar = this.avatar
    )
}