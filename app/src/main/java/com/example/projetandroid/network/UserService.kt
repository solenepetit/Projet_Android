package com.example.projetandroid.network

import com.example.projetandroid.login.LoginForm
import com.example.projetandroid.login.LoginResponse
import com.example.projetandroid.signup.SignupForm
import com.example.projetandroid.signup.SignupResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<LoginResponse>

    @POST("users/sign_up")
    suspend fun signup(@Body user: SignupForm): Response<SignupResponse>

}