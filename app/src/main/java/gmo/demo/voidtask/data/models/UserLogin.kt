package gmo.demo.voidtask.data.models

import java.io.Serializable

data class UserLogin(
    val id: Int = 0,
    val username: String? =null,
    val password: String? =null,
): Serializable