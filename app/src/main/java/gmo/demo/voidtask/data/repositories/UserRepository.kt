package gmo.demo.voidtask.data.repositories

import gmo.demo.voidtask.data.models.UserModel
import gmo.demo.voidtask.data.network.ServiceProvider

class UserRepository {
    private val authApi = ServiceProvider.authApi
    private var userList: List<UserModel>? = null

    suspend fun getUsers(): List<UserModel> {
        if (userList == null) {
            userList = authApi.getUsers().users
        }
        return userList ?: emptyList()
    }
}