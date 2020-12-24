package com.example.projetandroid.userinfo

import android.app.Application
import androidx.lifecycle.*
import com.example.projetandroid.database.getDatabase
import com.example.projetandroid.database.getUserDatabase
import com.example.projetandroid.login.LoginForm
import com.example.projetandroid.login.LoginResponse
import com.example.projetandroid.network.UserInfo
import com.example.projetandroid.signup.SignupForm
import com.example.projetandroid.signup.SignupResponse
import com.example.projetandroid.tasklist.TaskListViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body

class UserInfoViewModel(application : Application): AndroidViewModel(application) {
    private val repository = UserInfoRepository(getUserDatabase(application))
    private var _userInfo = MutableLiveData<UserInfo>()
    var userInfo : LiveData<UserInfo> = _userInfo

    suspend fun refresh() {repository.refresh()}

    suspend fun updateInfo(@Body userInfo : UserInfo): Response<UserInfo> {return repository.updateInfo(userInfo)}
    suspend fun updateAvatar(avatar: MultipartBody.Part) {repository.updateAvatar(avatar)}
    suspend fun login(@Body user: LoginForm): Response<LoginResponse> { return repository.login(user) }
    suspend fun signup(@Body user: SignupForm): Response<SignupResponse> {return repository.signup(user)}

    /**
     * Factory for constructing UserInfoViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserInfoViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}