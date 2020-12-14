package com.example.projetandroid.userinfo

import com.example.projetandroid.network.Api
import com.example.projetandroid.network.UserInfo
import okhttp3.MultipartBody

class UserInfoRepository {
    val userService = Api.userService

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = userService.getInfo()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            tasksResponse.body()
        }
        else null
    }

    suspend fun updateInfo(userInfo : UserInfo) {
        userService.update(userInfo)
    }

    suspend fun updateAvatar(avatar: MultipartBody.Part) {
        userService.updateAvatar(avatar)
    }
}