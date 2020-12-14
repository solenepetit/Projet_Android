package com.example.projetandroid.userinfo

import androidx.lifecycle.ViewModel
import com.example.projetandroid.network.UserInfo
import okhttp3.MultipartBody

class UserInfoViewModel : ViewModel() {
    private val repository = UserInfoRepository()
    private var userInfo : UserInfo? = null

    suspend fun refresh() {//repository.refresh()
        val infoResponse = repository.userService.getInfo()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (infoResponse.isSuccessful) {
            val fetchedInfo = infoResponse.body()
            userInfo = fetchedInfo!!
        }
        else null
    }

    suspend fun updateInfo(userInfo : UserInfo) {repository.updateInfo(userInfo)}
    suspend fun updateAvatar(avatar: MultipartBody.Part) {repository.updateAvatar(avatar)}
}