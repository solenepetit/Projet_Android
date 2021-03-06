package com.example.projetandroid.userinfo

import com.example.projetandroid.database.UsersDatabase
import com.example.projetandroid.login.LoginForm
import com.example.projetandroid.login.LoginResponse
import com.example.projetandroid.network.Api
import com.example.projetandroid.network.UserInfo
import com.example.projetandroid.network.asDatabaseModel
import com.example.projetandroid.signup.SignupForm
import com.example.projetandroid.signup.SignupResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body

class UserInfoRepository(private val database : UsersDatabase) {
    val userService = Api.INSTANCE.userService

    suspend fun refresh() {


        withContext(Dispatchers.IO) {
            // Call HTTP (opération longue):
            val usersResponse = userService.getInfo()
            // À la ligne suivante, on a reçu la réponse de l'API:
            if (usersResponse.isSuccessful) {
                database.userInfoDao.deleteAll()
                database.userInfoDao.insertAll(listOf(usersResponse.body()!!.asDatabaseModel()))
                //tasksResponse.body()
            }
            else null
        }
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

    suspend fun signup(@Body user: SignupForm): Response<SignupResponse> {
        return userService.signup(user)
    }

}