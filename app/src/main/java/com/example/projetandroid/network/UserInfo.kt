package com.example.projetandroid.network

import androidx.room.Entity
import com.example.projetandroid.database.DatabaseTask
import com.example.projetandroid.database.DatabaseUserInfo
import com.example.projetandroid.tasklist.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo (
    @SerialName("email")
    val email: String,
    @SerialName("firstname")
    val firstName: String,
    @SerialName("lastname")
    val lastName: String,
    @SerialName("avatar")
    val avatar : String = "https://goo.gl/gEgYUd"
)

fun UserInfo.asDatabaseModel() : DatabaseUserInfo {
    return DatabaseUserInfo(
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            avatar = this.avatar
    )
}
