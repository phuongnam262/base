package gmo.demo.voidtask.data.entities.responses

import gmo.demo.voidtask.data.models.UserModel

data class LoginResponse(
    val token: String,
    val user: UserModel
)