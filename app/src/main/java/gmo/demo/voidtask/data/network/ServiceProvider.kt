package gmo.demo.voidtask.data.network

import gmo.demo.voidtask.data.network.services.AuthApi

object ServiceProvider {
    val authApi: AuthApi by lazy {
        RetrofitInstance.retrofit.create(AuthApi::class.java)
    }
}