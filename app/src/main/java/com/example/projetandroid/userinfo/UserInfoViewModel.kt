package com.example.projetandroid.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetandroid.login.LoginForm
import com.example.projetandroid.login.LoginResponse
import com.example.projetandroid.network.UserInfo
import com.example.projetandroid.signup.SignupForm
import com.example.projetandroid.signup.SignupResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body

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

    suspend fun updateInfo(@Body userInfo : UserInfo): Response<UserInfo> {return repository.updateInfo(userInfo)}
    suspend fun updateAvatar(avatar: MultipartBody.Part) {repository.updateAvatar(avatar)}
    suspend fun login(@Body user: LoginForm): Response<LoginResponse> { return repository.login(user) }
    suspend fun signup(@Body user: SignupForm): Response<SignupResponse> {return repository.signup(user)}
}