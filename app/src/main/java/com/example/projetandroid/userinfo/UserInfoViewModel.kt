package com.example.projetandroid.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetandroid.network.UserInfo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel : ViewModel() {
    private val repository = UserInfoRepository()
    private var _userInfo = MutableLiveData<UserInfo>()
    var userInfo : LiveData<UserInfo> = _userInfo

    fun refresh() {//repository.refresh()
        viewModelScope.launch {
            val infoResponse = repository.userService.getInfo()
            // À la ligne suivante, on a reçu la réponse de l'API:
            if (infoResponse.isSuccessful) {
                val fetchedInfo = infoResponse.body()
                _userInfo.value = fetchedInfo!!
            }
        }
    }

    suspend fun updateInfo(userInfo : UserInfo) {repository.updateInfo(userInfo)}
    suspend fun updateAvatar(avatar: MultipartBody.Part) {repository.updateAvatar(avatar)}
}