package com.example.projetandroid

import android.app.Application
import com.example.projetandroid.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
    }
}