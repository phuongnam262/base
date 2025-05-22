package gmo.demo.voidtask.data.network.services

import gmo.demo.voidtask.data.entities.request.LoginRequest
import gmo.demo.voidtask.data.entities.request.VerifyOTPRequest
import gmo.demo.voidtask.data.entities.responses.BaseResponse
import gmo.demo.voidtask.data.entities.responses.ConsumerLoginResponse
import gmo.demo.voidtask.data.entities.responses.LoginResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import gmo.demo.voidtask.data.models.UserModel

interface AppServives {

    @POST("/api/verify-otp")
    suspend fun verifyOTP(
        @Body verifyOTPRequest: VerifyOTPRequest
    ): Response<BaseResponse<ConsumerLoginResponse>>

    // Thêm API đăng nhập
    @GET("users")
    suspend fun getUsers(): UserResponse

    companion object {
        fun create(retrofit: Retrofit): AppServives {
            return retrofit.create(AppServives::class.java)
        }
    }
    @POST("/api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>
}