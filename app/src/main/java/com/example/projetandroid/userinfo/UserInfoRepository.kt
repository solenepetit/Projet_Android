package com.example.projetandroid.userinfo

import com.example.projetandroid.login.LoginForm
import com.example.projetandroid.login.LoginResponse
import com.example.projetandroid.network.Api
import com.example.projetandroid.network.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body

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

    suspend fun updateInfo(@Body userInfo : UserInfo): Response<UserInfo> {
        return userService.update(userInfo)
    }

    suspend fun updateAvatar(avatar: MultipartBody.Part) {
        userService.updateAvatar(avatar)
    }

    suspend fun login(@Body user: LoginForm): Response<LoginResponse> {
        return userService.login(user)
    }
}