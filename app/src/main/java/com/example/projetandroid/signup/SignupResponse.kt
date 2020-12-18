package com.example.projetandroid.signup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
        @SerialName("token")
        val token:String
)
