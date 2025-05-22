package gmo.demo.voidtask.data.network.services

import gmo.demo.voidtask.data.models.UserModel
import retrofit2.http.GET

interface AuthApi {
    @GET("users")
    suspend fun getUsers(): UserResponse
}

data class UserResponse(
    val users: List<UserModel>
)