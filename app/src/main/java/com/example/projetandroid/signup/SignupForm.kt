package com.example.projetandroid.signup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupForm(
        @SerialName("firstname")
        val firstname: String,
        @SerialName("lastname")
        val lastname: String,
        @SerialName("email")
        val email: String,
        @SerialName("password")
        val password: String,
        @SerialName("password_confirmation")
        val password_confirmation: String
)
